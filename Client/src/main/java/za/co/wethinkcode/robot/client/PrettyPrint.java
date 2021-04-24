package za.co.wethinkcode.robot.client;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class PrettyPrint {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\033[1;91m";
    private static final String ANSI_GREEN = "\033[33;1m";
    private static final String ANSI_CYAN = "\033[35;1m";
    private static final String CYAN_UNDERLINED = "\033[4;33m";
    private final String messageFromServer;

    public PrettyPrint(String message) {
        this.messageFromServer = message;
    }


    public void printMessage() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject messageFromClient = (JSONObject) parser.parse(this.messageFromServer);
        System.out.println("___________________________________________________________");
        if (messageFromClient.get("result") != null) {
            switch (messageFromClient.get("result").toString()) {
                case "OK":

                    JSONObject data = (JSONObject) messageFromClient.get("data");
                    if (data.get("message") == null) {
                        if (data.get("mine") == null) {
                            lookPrint(messageFromClient);
                        } else {
                            launchPrint(messageFromClient);
                        }
                    } else if (data.get("message").equals("Hit")) {
                        hitPrint(messageFromClient);
                    } else {
                        simplePrint(messageFromClient);
                    }
                    break;
                case "ERROR":
                    errorPrint(messageFromClient);
                    break;
            }
            System.out.println("___________________________________________________________");
        } else {
            statePrint((JSONObject) messageFromClient.get("state"), "");
        }
    }

    /**
     * @param state is robot state. Checks for bullets left, shield left and location.
     * @param robot name chosen by the user.
     */
    private static void statePrint(JSONObject state, String robot) {
        if (robot.equalsIgnoreCase("enemy")) {
            System.out.println(CYAN_UNDERLINED + ANSI_CYAN + "Enemy State" + ANSI_RESET + "\n"
                    + ANSI_CYAN + "\t\tShields Left\t:\t" + ANSI_RESET + ANSI_GREEN + state.get("shields") + "\n"
                    + ANSI_CYAN + "\t\tPosition\t\t:\t" + ANSI_RESET + ANSI_GREEN + state.get("position") + "\n"
                    + ANSI_CYAN + "\t\tDirection\t\t:\t" + ANSI_RESET + ANSI_GREEN + state.get("direction") + "\n"
                    + ANSI_CYAN + "\t\tShots\t\t\t:\t" + ANSI_RESET + ANSI_GREEN + state.get("shots") + "\n"
                    + ANSI_CYAN + "\t\tStatus\t\t\t:\t" + ANSI_RESET + ANSI_GREEN + state.get("status") + "\n"
                    + ANSI_RESET);
        } else {
            System.out.println(CYAN_UNDERLINED + ANSI_CYAN + "Robot State" + ANSI_RESET + "\n"
                    + ANSI_CYAN + "\t\tShields Left\t:\t" + ANSI_RESET + ANSI_GREEN + state.get("shields") + "\n"
                    + ANSI_CYAN + "\t\tPosition\t\t:\t" + ANSI_RESET + ANSI_GREEN + state.get("position") + "\n"
                    + ANSI_CYAN + "\t\tDirection\t\t:\t" + ANSI_RESET + ANSI_GREEN + state.get("direction") + "\n"
                    + ANSI_CYAN + "\t\tShots\t\t\t:\t" + ANSI_RESET + ANSI_GREEN + state.get("shots") + "\n"
                    + ANSI_CYAN + "\t\tStatus\t\t\t:\t" + ANSI_RESET + ANSI_GREEN + state.get("status") + "\n"
                    + ANSI_RESET);
        }
    }

    /**
     * @param message is the response message when an invalid request was made.
     */
    private static void errorPrint(JSONObject message) {
        JSONObject data = (JSONObject) message.get("data");
        System.out.println(ANSI_CYAN + "Result\t\t\t\t\t\t:\t" + ANSI_RESET + ANSI_RED + message.get("result") + ", " + data.get("message") + ANSI_RESET + "\n");
    }

    /**
     * @param message initial message response sent after a robot launch was successful.
     */
    private static void launchPrint(JSONObject message) {
        JSONObject data = (JSONObject) message.get("data");
        System.out.println(CYAN_UNDERLINED + ANSI_CYAN + "System Data" + ANSI_RESET + "\n"
                + ANSI_CYAN + "\t\tMine Duration\t:\t" + ANSI_RESET + ANSI_GREEN + data.get("mine") + "\n"
                + ANSI_CYAN + "\t\tRepair Duration\t:\t" + ANSI_RESET + ANSI_GREEN + data.get("repair") + "\n"
                + ANSI_CYAN + "\t\tShields Number\t:\t" + ANSI_RESET + ANSI_GREEN + data.get("shields") + "\n"
                + ANSI_CYAN + "\t\tReload Duration\t:\t" + ANSI_RESET + ANSI_GREEN + data.get("reload") + "\n"
                + ANSI_CYAN + "\t\tVisible Area\t:\t" + ANSI_RESET + ANSI_GREEN + data.get("visibility") + "\n"
                + ANSI_RESET);
        statePrint((JSONObject) message.get("state"), "");
    }

    /**
     * @param message display of robot state and all possible obstacles five blocks from all directions
     */
    private static void lookPrint(JSONObject message) {
        JSONObject data = (JSONObject) message.get("data");
        JSONArray objects = (JSONArray) data.get("objects");
        System.out.println(CYAN_UNDERLINED + ANSI_CYAN + "Obstacles" + ANSI_RESET);
        for (Object object : objects) {
            JSONObject obs = (JSONObject) object;
            System.out.println(ANSI_CYAN + "\t\tType\t\t\t:\t" + ANSI_GREEN + obs.get("type") + ANSI_RESET);
            System.out.println(ANSI_CYAN + "\t\tDirection\t\t:\t" + ANSI_GREEN + obs.get("direction") + ANSI_RESET);
            System.out.println(ANSI_CYAN + "\t\tDistance\t\t:\t" + ANSI_GREEN + obs.get("distance") + ANSI_RESET);
            System.out.println();
        }
        statePrint((JSONObject) message.get("state"), "");
    }

    /**
     * The display message after a robot gets shot by another robot.
     *
     * @param message is the response from the server after every action.
     */
    private static void hitPrint(JSONObject message) {
        JSONObject data = (JSONObject) message.get("data");
        System.out.println(ANSI_CYAN + "\t\tResult\t\t:\t" + ANSI_RESET + ANSI_GREEN + message.get("result") + ANSI_RESET);
        System.out.println(ANSI_CYAN + "\t\tMessage\t\t\t:\t" + ANSI_RESET + ANSI_GREEN + data.get("message"));
        System.out.println(ANSI_CYAN + "\t\tEnemy\t\t\t:\t" + ANSI_RESET + ANSI_GREEN + data.get("robot"));
        System.out.println(ANSI_CYAN + "\t\tDistance\t\t:\t" + ANSI_RESET + ANSI_GREEN + data.get("distance") + "\n");
        statePrint((JSONObject) data.get("state"), "enemy");
        statePrint((JSONObject) message.get("state"), "");
    }

    /**
     * @param message is the command validation response for all request made by the robot.
     */
    private static void simplePrint(JSONObject message) {
        JSONObject data = (JSONObject) message.get("data");
        System.out.println(ANSI_CYAN + "\t\tResult\t\t\t:\t" + ANSI_RESET + ANSI_GREEN + message.get("result") + ANSI_RESET);
        System.out.println(ANSI_CYAN + "\t\tMessage\t\t\t:\t" + ANSI_RESET + ANSI_GREEN + data.get("message") + "\n");
        statePrint((JSONObject) message.get("state"), "");
    }
}