package za.co.wethinkcode.robot.server;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Display.Draw;
import za.co.wethinkcode.robot.server.Robot.Robot;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ServerManagement implements Runnable {
    //Ansi escape codes to be used for pretty printing.
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32;1m";
    public static final String ANSI_BLUE = "\u001B[34;1m";
    public static final String ANSI_PURPLE = "\u001B[35;1m";
    public static final String ANSI_CYAN = "\u001B[36m";
    //Display to be drawn on for the dump command.
    private final Draw display;
    private final Scanner sc;
    private final World world;
    boolean running;

    public ServerManagement(World world) {
        this.sc = new Scanner(System.in);
        this.world = world;
        running = true;
        this.display = new Draw();
    }

    public void run() {
        //Sleep for 3 seconds due to threads printing at the same time when run on fast CPU's.
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Pretty print the instructions for using the server to the server admin.
        System.out.println("Server is running and live!\n" +
                ANSI_PURPLE +
                "Server can message clients individually by using the /message tag.\n" +
                ANSI_GREEN + "       eg. /message <clientName> <message>\n" +
                ANSI_PURPLE + "Server can issue commands using the /command tag.\n" +
                ANSI_GREEN +"       eg. /command <command> <tag>\n" + ANSI_RESET +
                ANSI_GREEN+"       <purge> <client-name>"+ANSI_RESET+" - Purges the selected user from the server.\n" +
                ANSI_GREEN+"       <clients> <>         "+ANSI_RESET+" - Lists all the currently connected users and their username.\n" +
                ANSI_GREEN+"       <robots> <>          "+ANSI_RESET+" - Lists the robots currently on the map and their states.\n" +
                ANSI_GREEN+"       <quit> <>            "+ANSI_RESET+" - Closes all currently connected clients and threads. Quits program.");

        while (running) {
            String serverMessage = sc.nextLine();
            //Split the console input into a string.
            List<String> inputString = Arrays.asList(serverMessage.split(" ", 2));

            //execute server commands that will alter the world.
            switch (inputString.get(0)) {
                case "quit":
                    quitServer();
                    break;
                case "robots":
                    listRobots();
                    break;
                case "purge":
                    purgeUser(inputString.get(1));
                    break;
                case "clients":
                    showUsers();
                    break;
                case "dump":
                    dump();
                    break;
            }
        }
    }

    /**
     * Method will close all the threads currently running on the server,
     * then it will close the server completely.
     */
    private void quitServer() {
        //Loops through the client list and closes the.
        for (Server client : MultiServer.clients) {
            client.closeThread();
        }
        this.running = false;
        System.exit(69);
    }

    /**
     * Method will display all the robots currently in play on the field,
     * as well as their current states.
     */
    private void listRobots() {
        //Creates a copy of the robots HashMap
        ConcurrentHashMap<String, Robot> robotDict = world.getRobots();
        //Create a set with the keys from the HashMap
        Set<String> robots = robotDict.keySet();

        //Loops through the list of robots and grabs the values.
        for (String key : robots) {
            Robot currentRobot = robotDict.get(key);
            JSONObject robot = currentRobot.getState();
            //Pretty prints the state of the current robot in the loop.
            System.out.println(ANSI_GREEN + "\t\t\t\tName\t\t:\t" + ANSI_CYAN + currentRobot.getName() + ANSI_RESET);
            System.out.println(ANSI_GREEN + "\t\t\t\tPosition\t:\t" + ANSI_CYAN + robot.get("position") + ANSI_RESET);
            System.out.println(ANSI_GREEN + "\t\t\t\tDirection\t:\t" + ANSI_CYAN + robot.get("direction") + ANSI_RESET);
            System.out.println(ANSI_GREEN + "\t\t\t\tShield\t\t:\t" + ANSI_CYAN + robot.get("shields") + ANSI_RESET);
            System.out.println(ANSI_GREEN + "\t\t\t\tShots\t\t:\t" + ANSI_CYAN + robot.get("shots") + ANSI_RESET);
            System.out.println(ANSI_GREEN + "\t\t\t\tStatus\t\t:\t" + ANSI_CYAN + robot.get("status") + ANSI_RESET);
            System.out.println(ANSI_GREEN + "\t\t\t\t_____\t\t:\t" + ANSI_CYAN + "_____" + ANSI_RESET);
        }
    }

    /**
     * Removes the user with the given name from the robots list as well as client list.
     * @param username that will be purged.
     */
    private void purgeUser(String username) {
        for (Server client:MultiServer.clients) {
            if (client.robotName.equalsIgnoreCase(username)) {
                client.robot.kill(world, client, "Bonk");
                break;
            }
        }
    }

    /**
     * Prints the currently connected clients to the console.
     */
    private void showUsers() {
        for (Server client : MultiServer.clients) {
            System.out.println(client.robotName + ": " + client);
        }
    }

    /**
     * Creates a turtle field that will be used to display the objects on the field.
     * Objects shown will as follows.
     * Mines in Red.
     * Robots in Green.
     * Obstacles in Purple.
     * Bottomless pits in Black.
     */
    private void dump() {
        //Clears the display before printing out.
        display.clear();
        // Calls the methods to display the separate objects on the field.
        display.drawObstacles(MultiServer.world.getObstacles(), Color.MAGENTA);
        display.drawObstacles(MultiServer.world.getMaze().getPits(), Color.BLACK);
        display.drawObstacles(MultiServer.world.getMaze().getMines(), Color.RED);
        display.drawRobots(MultiServer.world.getRobots(), Color.GREEN);
    }
}
