import java.util.List;

public class SetValues {

    public static int setApplicationID(String app) {

        int appID;

        switch (app) {
            case "Accounting":
                appID = 54;
                break;
            case "Admissions":
                appID = 52;
                break;
            case "BYUI Tickets":
                appID = 40;
                break;
            case "Employment":
                appID = 30;
                break;
            case "Financial Aid":
                appID = 53;
                break;
            case "Lost and Found":
                appID = 60;
                break;
            case "(BSC) Operations":
                appID = 42;
                break;
            case "(BSC) Outreach":
                appID = 39;
                break;
            case "System and Dept":
                appID = 31;
                break;
            case "SRR":
                appID = 55;
                break;
            case "I-Learn":
                appID = 44;
                break;
            default:
                return 0;
        }

        return appID;

    }

    public static int setStatusClosedID(String app) {

        int statusID;

        switch (app) {

            case "Accounting":
                statusID = 417;
                return statusID;
            case "Admissions":
                statusID = 403;
                return statusID;
            case "BYUI Tickets":
                statusID = 200;
                return statusID;
            case "Employment":
                statusID = 35;
                return statusID;
            case "Financial Aid":
                statusID = 410;
                return statusID;
            case "Lost and Found":
                statusID = 510;
                return statusID;
            case "Operations":
                statusID = 214;
                return statusID;
            case "(BSC) Outreach":
                statusID = 179;
                return statusID;
            case "System and Dept":
                statusID = 42;
                return statusID;
            case "SRR":
                statusID = 424;
                return statusID;
            case "I-Learn":
                statusID = 227;
                return statusID;
            default:
                System.out.println("Invalid Status!");
                return 0;   // 0 will be known for its error in application
        }
    }

    public static int setStatusCancelledID(String app) {

        int statusID;

        switch (app) {

            case "Accounting":
                statusID = 418;
                return statusID;
            case "Admissions":
                statusID = 404;
                return statusID;
            case "BYUI Tickets":
                statusID = 201;
                return statusID;
            case "Employment":
                statusID = 36;
                return statusID;
            case "Financial Aid":
                statusID = 411;
                return statusID;
            case "Lost and Found":
                statusID = 511;
                return statusID;
            case "Operations":
                statusID = 215;
                return statusID;
            case "(BSC) Outreach":
                statusID = 180;
                return statusID;
            case "System and Dept":
                statusID = 43;
                return statusID;
            case "SRR":
                statusID = 425;
                return statusID;
            case "I-Learn":
                statusID = 229;
                return statusID;
            default:
                System.out.println("Invalid Status!");
                return 0;   // 0 will be known for its error in application
        }
    }

    public static int setAccountDepartment(String app) {

        int acct = 0;

        return acct;
    }


}
