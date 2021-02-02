import td.api.JsonPatchArray;
import td.api.Report;
import td.api.TDException;
import td.api.TeamDynamix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

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

    /**
     * Edit Actual Tickets
     * Description:
     *
     *
     * @Param List ticketIDsList
     * @Param List appIDsList
     * @Param List cleanupAttributeList
     * @Param String cleanUpType
     * @Param TeamDynamix tdapi
     */
    public static void editActualTickets(List<Integer> ticketIDsList, List<Integer> appIDsList,
                                         List<Integer> cleanupAttributeList, String cleanUpType,
                                         TeamDynamix tdapi) {

        // JsonPatchArray is a class in the api package
        JsonPatchArray patch = new JsonPatchArray();

        // What kind of cleanup is it?
        String path = "";
        switch (cleanUpType) {
            case "LOCATION":
                path = "LocationID";
                break;
            case "CLOSED":
                path = "StatusID";
                break;
            case "CANCELLED":
                path = "StatusID";
                break;
            case "ACCT":
                path = "AccountID";
                break;
        }

        for (int i = 0; i < ticketIDsList.size(); i++) {

            patch.addPatchOperation(JsonPatchArray.ADD_OP, path, cleanupAttributeList.get(i));
            // This will patch the ticket
            try {
                tdapi.patchTicket(appIDsList.get(i), false, ticketIDsList.get(i), patch);
            } catch (TDException e) {
                e.printStackTrace();
            }

            // forces program to do one request per second to prevent rate limit errors
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("The ticketID edited is: " + ticketIDsList.get(i));

        }
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
        List<Integer> ticketIDsList = new ArrayList<>();
        List<Integer> appIDsList = new ArrayList<>();
        List<Integer> errorList = new ArrayList<>();
        List<Integer> cleanUpAttributeList = new ArrayList<>();
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
                    ticketIDsList.add(value);
                    currentTicket = (value);

                }
                if (key.equals("AppName")) {

                    String appID = row.get(key);
                    appIDsList.add(setValues.setApplicationID(appID));
                    // add data to clean up attribute list
                    setValues.addToCleanUpAttributeList(cleanUpAttributeList, cleanUpType, row.get(key));

                    if (setValues.setApplicationID(row.get(key)) == 0)
                    {
                        System.out.println("Invalid Application! TicketID: " + currentTicket);
                        errorList.add(currentTicket);

                        // remove added data because there was an error
                        ticketIDsList.remove(ticketIDsList.size() - 1);
                        appIDsList.remove(appIDsList.size() - 1);
                        cleanUpAttributeList.remove(cleanUpAttributeList.size() - 1);
                    }
                }
            }
        }

        System.out.println("The amount of successfully read tickets in the report is: " + ticketIDsList.size());
        // displayIntegerList(cleanUpAttributeList);

        // Are there errors in the read?
        if (!errorList.isEmpty()) {
            error(errorList);
        }
        editActualTickets(ticketIDsList, appIDsList, cleanUpAttributeList, cleanUpType, tdapi);
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
        System.out.println(YELLOW + "the ID, Application, and the Acct/Dept. If any of those");
        System.out.println(YELLOW + "are missing the code will fail. It depends on those to");
        System.out.println(YELLOW + "edit the tickets in the report. Add columns if necessary");

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
//        readReportData(report, cleanUpType, tdapi);
    }
}
