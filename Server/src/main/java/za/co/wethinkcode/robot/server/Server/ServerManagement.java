package za.co.wethinkcode.robot.server.Server;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.persistence.Database;
import za.co.wethinkcode.robot.server.Robot.Robot;
import za.co.wethinkcode.robot.server.World;

import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ServerManagement implements Runnable {
    //Ansi escape codes to be used for pretty printing.
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32;1m";
    public static final String ANSI_PURPLE = "\u001B[35;1m";
    public static final String ANSI_CYAN = "\u001B[36m";
    //Display to be drawn on for the dump command.
    private final Scanner sc;
    private final World world;
    private Database database;
    boolean running;

    public ServerManagement(World world) throws SQLException {
        this.sc = new Scanner(System.in);
        this.world = world;
        running = true;
        this.database = new Database("jdbc:sqlite:uss_victory_db.sqlite");
    }

    public void run() {
        //Sleep for 3 seconds due to threads printing at the same time when run on fast CPU's.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Pretty print the instructions for using the server to the server admin.
        serverCommandsPrint();

        while (running) {
            try {
                String serverMessage = sc.nextLine();
                //Split the console input into a string.
                List<String> inputString = Arrays.asList(serverMessage.split(" ", 2));

                //execute server commands that will alter the world.
                if(inputString.size() == 1){
                    singleArgCommand(inputString);
                }else{
                    multiArgCommands(inputString);
                }

            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
    }

    /**
     * Method will close all the threads currently running on the server,
     * then it will close the server completely.
     */
    private void quitServer() {
        //Loops through the client list and closes their threads.
        try {
            for (Server client : MultiServer.clients) {
                client.closeThread();
            }
        } catch (Exception ignored) {}
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
                client.robot.setActivity(false);
//                client.robot.kill(world, client, "Bonk");
                System.out.println("Purged user!");
                break;
            }
        }
        try {
            world.removeRobot(username);
        } catch (NullPointerException ignored) {}
    }

    /**
     * Removes all clients connected to the server.
     */
    private void purgeAllUsers(){
        try {
            for (Server client : MultiServer.clients) {
                client.closeThread();
            }
        } catch (Exception ignored) {}
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
    }

    /**
     * Takes in a list of user input commands one string and uses a switch
     * To determine which command functions to run.
     * @param inputString List<String>
     */
    private void singleArgCommand(List<String> inputString){

        switch (inputString.get(0)) {
            case "quit":
                quitServer();
                System.out.println("Quiting the server!");
                break;
            case "robots":
                listRobots();
                System.out.println("Listed robots!");
                break;
            case "clients":
                showUsers();
                System.out.println("Showed users!");
                break;
            case "dump":
                dump();
                System.out.println("Displayed to Turtle!");
                break;
        }
    }

    /**
     * Takes in a list of user input commands that contains more than one string and uses a switch
     * To determine which command functions to run.
     * @param inputString List<String>
     * @throws SQLException exception
     */
    private void multiArgCommands(List<String> inputString) throws SQLException {
        switch (inputString.get(0)) {
            case "purge":
                purgeUser(inputString.get(1));
                break;
            case "save":
                database.saveWorld(world, inputString.get(1), MultiServer.getWorldSize());
                break;
            case "restore":
                if(database.readWorld(world, inputString.get(1))){
                    purgeAllUsers();
                }
                break;
        }
    }

    /**
     * Prints a list of all server commands to the terminal.
     */
    private void serverCommandsPrint(){
        System.out.println("Server is running and live!\n" +
                ANSI_PURPLE+"Server can issue commands using the command name.\n" +
                ANSI_GREEN+"       eg. <command> <tag>\n" + ANSI_RESET +
                ANSI_GREEN+"       <purge> <client-name>"+ANSI_RESET+" - Purges the selected user from the server.\n" +
                ANSI_GREEN+"       <save> <save-name>"        +ANSI_RESET+" - Saves the current worlds size along with all obstacles to a database.\n" +
                ANSI_GREEN+"       <restore> <saved-name>"     +ANSI_RESET+" - Restores the world to a previously saved state from the database if it exists in the database.\n" +
                ANSI_GREEN+"       <clients> <>         "+ANSI_RESET+" - Lists all the currently connected users and their username.\n" +
                ANSI_GREEN+"       <robots> <>          "+ANSI_RESET+" - Lists the robots currently on the map and their states.\n" +
                ANSI_GREEN+"       <quit> <>            "+ANSI_RESET+" - Closes all currently connected clients and threads. Quits program.");
    }
}
