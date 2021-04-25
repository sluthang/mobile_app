package za.co.wethinkcode.robot.client;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class PrettyPrint {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\033[1;91m";
    private static final String ANSI_CYAN = "\033[33;1m";
    private static final String ANSI_PURPLE = "\033[35;1m";
    private static final String PURPLE_UNDERLINED = "\033[4;33m";
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
                            printLook(messageFromClient);
                        } else {
                            printLaunch(messageFromClient);
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
     * @param state the current robots state object, which will be used to print elements.
     * @param robot name of the robot.
     */
    private static void statePrint(JSONObject state, String robot) {
        if (robot.equalsIgnoreCase("enemy")) {
            System.out.println(PURPLE_UNDERLINED + ANSI_PURPLE + "Enemy State" + ANSI_RESET + "\n"
                    + ANSI_PURPLE + "\t\tShields Left\t\t:\t" + ANSI_RESET + ANSI_CYAN + state.get("shields") + "\n"
                    + ANSI_PURPLE + "\t\tPosition\t\t:\t" + ANSI_RESET + ANSI_CYAN + state.get("position") + "\n"
                    + ANSI_PURPLE + "\t\tDirection\t\t:\t" + ANSI_RESET + ANSI_CYAN + state.get("direction") + "\n"
                    + ANSI_PURPLE + "\t\tShots\t\t\t:\t" + ANSI_RESET + ANSI_CYAN + state.get("shots") + "\n"
                    + ANSI_PURPLE + "\t\tStatus\t\t\t:\t" + ANSI_RESET + ANSI_CYAN + state.get("status") + "\n"
                    + ANSI_RESET);
        } else {
            System.out.println(PURPLE_UNDERLINED + ANSI_PURPLE + "Robot State" + ANSI_RESET + "\n"
                    + ANSI_PURPLE + "\t\tShields Left\t\t:\t" + ANSI_RESET + ANSI_CYAN + state.get("shields") + "\n"
                    + ANSI_PURPLE + "\t\tPosition\t\t:\t" + ANSI_RESET + ANSI_CYAN + state.get("position") + "\n"
                    + ANSI_PURPLE + "\t\tDirection\t\t:\t" + ANSI_RESET + ANSI_CYAN + state.get("direction") + "\n"
                    + ANSI_PURPLE + "\t\tShots\t\t\t:\t" + ANSI_RESET + ANSI_CYAN + state.get("shots") + "\n"
                    + ANSI_PURPLE + "\t\tStatus\t\t\t:\t" + ANSI_RESET + ANSI_CYAN + state.get("status") + "\n"
                    + ANSI_RESET);
        }
    }

    /**
     * @param message the object returned when an invalid request is made.
     */
    private static void errorPrint(JSONObject message) {
        JSONObject data = (JSONObject) message.get("data");
        System.out.println(ANSI_PURPLE + "Result\t\t\t\t\t\t:\t" + ANSI_RESET + ANSI_RED + message.get("result") + ", " + data.get("message") + ANSI_RESET + "\n");
    }

    /**
     * @param message the message received after a robot is successfully launched.
     */
    private static void printLaunch(JSONObject message) {
        JSONObject data = (JSONObject) message.get("data");
        System.out.println(PURPLE_UNDERLINED + ANSI_PURPLE + "System Data" + ANSI_RESET + "\n"
                + ANSI_PURPLE + "\t\tMine Duration\t:\t" + ANSI_RESET + ANSI_CYAN + data.get("mine") + "\n"
                + ANSI_PURPLE + "\t\tRepair Duration\t:\t" + ANSI_RESET + ANSI_CYAN + data.get("repair") + "\n"
                + ANSI_PURPLE + "\t\tShields Number\t:\t" + ANSI_RESET + ANSI_CYAN + data.get("shields") + "\n"
                + ANSI_PURPLE + "\t\tReload Duration\t:\t" + ANSI_RESET + ANSI_CYAN + data.get("reload") + "\n"
                + ANSI_PURPLE + "\t\tVisible Area\t:\t" + ANSI_RESET + ANSI_CYAN + data.get("visibility") + "\n"
                + ANSI_RESET);
        statePrint((JSONObject) message.get("state"), "");
    }

    /**
     * @param message message received with an array of obstacles in all direction. This will be broken down and
     *                displayed to the user individually.
     */
    private static void printLook(JSONObject message) {
        JSONObject data = (JSONObject) message.get("data");
        JSONArray objects = (JSONArray) data.get("objects");
        System.out.println(PURPLE_UNDERLINED + ANSI_PURPLE + "Obstacles" + ANSI_RESET);
        for (Object object : objects) {
            JSONObject obs = (JSONObject) object;
            System.out.println(ANSI_PURPLE + "\t\tType\t\t\t:\t" + ANSI_CYAN + obs.get("type") + ANSI_RESET);
            System.out.println(ANSI_PURPLE + "\t\tDirection\t\t:\t" + ANSI_CYAN + obs.get("direction") + ANSI_RESET);
            System.out.println(ANSI_PURPLE + "\t\tDistance\t\t:\t" + ANSI_CYAN + obs.get("distance") + ANSI_RESET);
            System.out.println();
        }
        statePrint((JSONObject) message.get("state"), "");
    }

    /**
     * THe Object received when a robot is hit when firing.
     *
     * @param message the message received from the server
     */
    private static void hitPrint(JSONObject message) {
        JSONObject data = (JSONObject) message.get("data");
        System.out.println(ANSI_PURPLE + "\t\tResult\t\t:\t" + ANSI_RESET + ANSI_CYAN + message.get("result") + ANSI_RESET);
        System.out.println(ANSI_PURPLE + "\t\tMessage\t\t\t:\t" + ANSI_RESET + ANSI_CYAN + data.get("message"));
        System.out.println(ANSI_PURPLE + "\t\tEnemy\t\t\t:\t" + ANSI_RESET + ANSI_CYAN + data.get("robot"));
        System.out.println(ANSI_PURPLE + "\t\tDistance\t\t:\t" + ANSI_RESET + ANSI_CYAN + data.get("distance") + "\n");
        statePrint((JSONObject) data.get("state"), "enemy");
        statePrint((JSONObject) message.get("state"), "");
    }

    /**
     * @param message Validation of the command sent through to the server.
     */
    private static void simplePrint(JSONObject message) {
        JSONObject data = (JSONObject) message.get("data");
        System.out.println(ANSI_PURPLE + "\t\tResult\t\t\t:\t" + ANSI_RESET + ANSI_CYAN + message.get("result") + ANSI_RESET);
        System.out.println(ANSI_PURPLE + "\t\tMessage\t\t\t:\t" + ANSI_RESET + ANSI_CYAN + data.get("message"));
        System.out.println(ANSI_RESET +"___________________________________________________________" + ANSI_RESET);
        statePrint((JSONObject) message.get("state"), "");
    }
}