import td.api.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Starts up the program and edits the tickets
 *
 * @author shawnphillips
 * @since 02/04/2020
 */
public class Main {

    public static final String YELLOW = "\033[0;33m"; // YELLOW COLOR
    public static final String WHITE = "\033[0;37m";   // WHITE COLOR

    /**
     * Error
     * Description:
     *     Displays tickets that had an error being read in.
     *
     * @Param List errorList
     */
    public static void error(List<Integer> errorList) {

        String userInput = "";
        Scanner sc = new Scanner(System.in);
        System.out.println("The amount of errors are: " + errorList.size());
        boolean validInput = false;

        while(!validInput) {

            // See list of tickets?
            System.out.println("Would you like to see the error tickets? y/n");
            userInput = sc.nextLine();
            userInput = userInput.toUpperCase();

            if (userInput.equals("Y") || userInput.equals("N")) {
                validInput = true;
            }

            if (!validInput) {
                System.out.println("Invalid Input!");
            }
        }

        // Outputs all tickets in errorList
        if (userInput.equals("Y")) {
            for (Integer num: errorList) {
                System.out.println(num);
            }
        }
    }

    // Used for testing purposes
    public static void displayIntegerList(List<Integer> myList) {

        for (Integer num: myList) {
            System.out.println(num);
        }
    }

    public static void getRequestorTrack(Ticket ticket) {

    }
    /**
     * Read Report Data
     * Description:
     *     Loops through the passed in report and assigns some ticket data to Lists. These lists will be used
     *     to edit the actual tickets for there API calls.
     *
     * @Param Report report
     * @Param String cleanUpType
     * @Param TeamDynamix tdapi
     */
    public static void readReportData(Report report, String cleanUpType, TeamDynamix tdapi) {

        // Variables to set up while reading the report
        List<Integer> errorList = new ArrayList<>();
        Ticket ticket = new Ticket();
        int currentTicket = 0;

        SetValues setValues = new SetValues();

        // Read report and split in lists
        for (Map<String, String> row : report.getDataRows()) {  // Row
            for (String key : row.keySet()) { // Column

                // System.out.println("Key: " + key + " Value: " + row.get(key));

                if (key.equals("TicketID")) {

                    // row.get(key) saves the ticketID in a string like "123456.0"
                    // this will lose the floating point number
                    int value = (int) Float.parseFloat(row.get(key));
                    currentTicket = (value);
                }
                if (key.equals("AppName")) {

                    int currentApplicationID = setValues.setApplicationID(row.get(key));
//                    // add data to clean up attribute list
                    try {
                        ticket = tdapi.getTicket(currentApplicationID, currentTicket);
                    } catch (TDException e) {
                        e.printStackTrace();
                    }
                    // Set list of error tickets
                    if (setValues.setApplicationID(row.get(key)) == 0) {
                        System.out.println("Invalid Application! TicketID: " + currentTicket);
                        errorList.add(currentTicket);
                    } else {
                        // Clean up data. Key in this statement is the application ID

                        switch (cleanUpType) {
                            case "LOCATION":
                                ticket.setLocationId(1);
                            case "CLOSED":
                                ticket.setStatusId(setValues.setStatusClosedID(key));
                            case "CANCELLED":
                                ticket.setStatusId(setValues.setStatusCancelledID(key));
                            case "ACCT":
                                getRequestorTrack(ticket);
                            default:
                                break;
                        }
                    }
                }
                // wipe temp ticket data
                ticket = null;
            }

            // Are there errors in the read?
            if (!errorList.isEmpty()) {
                error(errorList);
            }
        }
    }

    /**
     * Main
     * Description:
     *     Starts to program, Logs into TD, Gets the Report, and Calls for a cleanup.
     */
    public static void main(String[] args) {

        String basePath = System.getenv("tdbasepath");
        String username = System.getenv("tduser");
        String password = System.getenv("tdpassword");

        // Objects and Variable Declarations
        TeamDynamix tdapi = new TeamDynamix(basePath, username, password);
        UserInterface ui = new UserInterface();
        Report report = null;
        boolean validReport = false;

        // Message to the User on the Console
        System.out.println(YELLOW + "For this project to work, the report entered will need");
        System.out.println(YELLOW + "the Ticket ID, and Application as columns. If any of those");
        System.out.println(YELLOW + "are missing the code will fail. It depends on those to");
        System.out.println(YELLOW + "edit the tickets in the report. ADD COLUMNS IF NECESSARY!");

        // Login to TD
        try {
            tdapi.login();
            System.out.println(WHITE + "Login Successful!");
        } catch (TDException e) {
            e.printStackTrace();
        }

        // Get valid TD report
        while (!validReport){

            // TODO: removed hard coded report
            try {
                int reportID = 15495;// ui.getReportID();
                report = tdapi.getReport(reportID, true, null);
                validReport = true;
            } catch (TDException e) {
                e.printStackTrace();
                validReport = false;
            }
        }

        // TODO: Remove hard coded cleanup type
        String cleanUpType =  "LOCATION"; // ui.cleanUpType();
        // clean up, clean up, everybody clean up!
        readReportData(report, cleanUpType, tdapi);
    }
}

