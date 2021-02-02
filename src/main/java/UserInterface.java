import java.util.Scanner;

/**
 * Class for User Interface
 *
 * @author shawnphillips
 * @since 02/02/2021
 */
public class UserInterface {

    /**
     * Get Report ID
     * Description:
     *     Prompts the user for an ID of a Report
     *
     */
    public static int getReportID() {

        String report;

        Scanner sc = new Scanner(System.in);
        System.out.println("Please Enter the Report ID: ");
        report = sc.nextLine();

        // converts String to an Int
        return Integer.parseInt(report);
    }


    /**
     * Clean Up Type
     * Description:
     *     Prompts the user for they type of cleanup project they would like to perform.
     *
     */
    public static String cleanUpType() {

        String cleanUpType = "";
        int input = 0;
        Scanner sc = new Scanner(System.in);
        boolean successfulInput = false;

        while (!successfulInput) {

            System.out.println("Please type the number for the cleanup project:");
            System.out.println("1: Add Location");
            System.out.println("2: Closed Status");
            System.out.println("3: Cancelled Status");
            System.out.println("4: Acct/Dept");
            input = sc.nextInt();

            switch (input) {
                case 1:
                    successfulInput = true;
                    cleanUpType = "LOCATION";
                    break;
                case 2:
                    successfulInput = true;
                    cleanUpType = "CLOSED";
                    break;
                case 3:
                    successfulInput = true;
                    cleanUpType = "CANCELLED";
                    break;
                case 4:
                    successfulInput = true;
                    cleanUpType = "ACCT";
                    break;
                default:
                    successfulInput = false;
                    cleanUpType = "ERROR";
                    System.out.println("ERROR: Please input a valid number");
                    break;
            }
        }

        return cleanUpType;
    }


}
