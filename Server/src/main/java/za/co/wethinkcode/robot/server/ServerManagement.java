package za.co.wethinkcode.robot.server;

import za.co.wethinkcode.robot.server.Display.Draw;
import za.co.wethinkcode.robot.server.Robot.Robot;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ServerManagement implements Runnable {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32;1m";
    public static final String ANSI_BLUE = "\u001B[34;1m";
    public static final String ANSI_PURPLE = "\u001B[35;1m";
    private final Draw display;
    private final Scanner sc;
    boolean running;

    public ServerManagement() {
        this.sc = new Scanner(System.in);
        running = true;
        this.display = new Draw();
    }

    public void run() {

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
                    case "dump":
                        dump();
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
                //noinspection RedundantCollectionOperation
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

    private void dump() {
        display.clear();

        display.drawObstacles(MultiServer.world.getObstacles(), Color.MAGENTA);
        display.drawObstacles(MultiServer.world.getMaze().getPits(), Color.BLACK);
        display.drawObstacles(MultiServer.world.getMaze().getMines(), Color.RED);
        display.drawRobots(MultiServer.world.getRobots(), Color.GREEN);
    }
}
