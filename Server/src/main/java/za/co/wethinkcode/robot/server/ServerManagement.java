package za.co.wethinkcode.robot.server;

import za.co.wethinkcode.robot.server.Robot.Robot;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServerManagement implements Runnable {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    private final Scanner sc;
    boolean running;

    public ServerManagement() {

        this.sc = new Scanner(System.in);
        running = true;
    }

    public void run() {
        while (running) {

            System.out.println("Server is running and live!\n" +
                    ServerManagement.ANSI_CYAN +
                    "Server can message clients individually by using the /message tag.\n" +
                    ServerManagement.ANSI_RESET +
                    ServerManagement.ANSI_YELLOW +
                    "       eg. /message <clientname> <message>\n" +
                    ServerManagement.ANSI_RESET +
                    ServerManagement.ANSI_CYAN +
                    "Server can issue commands using the /command tag.\n" +
                    ServerManagement.ANSI_RESET +
                    ServerManagement.ANSI_PURPLE +
                    "       eg. /command <command> <tag>\n" +
                    "       <purge> <client-name> - Purges the selected user from the server.\n" +
                    "       <clients> <>          - Lists all the currently connected users and their username.\n" +
                    "       <robots> <>           - Lists the robots currently on the map and their states.\n" +
                    "       <quit> <>             -  Closes all currently connected clients and threads. Quits program.\n" +
                    ServerManagement.ANSI_RESET);

            String serverMessage = sc.nextLine();
            List<String> inputString = Arrays.asList(serverMessage.split(" ", 3));

            // if /message is used a direct message is sent to a user. the username must be included.
            if (inputString.get(0).equals("/message")) {
                // /message <username> <message>, will fix later.
                for (Server client : MultiServer.clients) {
                    if (inputString.get(1).equals(client.clientName)) {
                        client.out.println(ANSI_BLUE + inputString.get(2) + ANSI_RESET);
                        client.out.flush();
                        System.out.println("Message sent to: " + ANSI_GREEN +
                                " "+ serverMessage + " " + client.clientName + ANSI_RESET);
                        break;
                    }
                }

                // If /command is used it will issue server side methods.
            } else if (inputString.get(0).equals("/command")) {
                //execute server commands that will alter the world.
                switch (inputString.get(1)) {
                    case "quit":
                        quitServer();
                        break;
                    case "robots":
                        listRobots();
                        break;
                    case "purge":
                        purgeUser(inputString.get(2));
                        break;
                    case "clients":
                        showUsers();
                        break;
                }
            }
        }
    }

    private void quitServer() {
        for (Server client : MultiServer.clients) {
            client.closeThread();
        }
        this.running = false;
        System.exit(0);
    }

    private void listRobots() {
        ConcurrentHashMap<String, Robot> robotDict = MultiServer.world.getRobots();
        Set<String> robots = robotDict.keySet();

        for (String key:robots) {
            Robot robot = robotDict.get(key);
            System.out.println("Robot name: "+ANSI_PURPLE+robot.getName()+" position: ("+robot.getPosition().getX()+
                    ","+robot.getPosition().getY()+") Direction: "+robot.getCurrentDirection()+ANSI_RESET);
        }
    }

    private void purgeUser(String username) {
        for (Server client:MultiServer.clients) {
            if (client.clientName.equalsIgnoreCase(username)) {
                client.closeThread();
                MultiServer.clients.remove(MultiServer.clients.indexOf(client));
                MultiServer.world.removeRobot(username);
                break;
            }
        }
    }

    private void showUsers() {
        for (Server client : MultiServer.clients) {
            System.out.println(client.clientName + ": " + client);
        }
    }
}
