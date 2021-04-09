package za.co.wethinkcode.robot.server;

import za.co.wethinkcode.robot.server.Robot.Robot;

import java.util.*;

public class ServerManagement implements Runnable {
    private final Scanner sc;
    boolean running;

    public ServerManagement() {
        this.sc = new Scanner(System.in);
        running = true;
    }

    public void run() {
        while (running) {
            String serverMessage = sc.nextLine();
            List<String> inputString = Arrays.asList(serverMessage.split(" ", 3));
            System.out.println(inputString);

            // if /message is used a direct message is sent to a user. the username must be included.
            if (inputString.get(0).equals("/message")) {
                // /message <username> <message>, will fix later.
                for (Server client : MultiServer.clients) {
                    if (inputString.get(1).equals(client.clientName)) {
                        client.out.println(inputString.get(2));
                        client.out.flush();
                        System.out.println("Message sent to: " +
                                " "+ serverMessage + " " + client.clientName);
                        break;
                    }
                }
                // If /command is used it will issue server side methods.
            } else if (inputString.get(0).equals("/command")) {
                //execute server commands that will alter the world.
                if (inputString.get(1).equals("quit")) {
                    quitServer();
                } else if (inputString.get(1).equals("robots")) {
                    listRobots();
                } else if (inputString.get(1).equals("purge")) {
                    purgeUser(inputString.get(2));
                } else if (inputString.get(1).equals("clients")) {
                    showUsers();
                }
                System.out.println("Issues command.");
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
        Hashtable<String, Robot> robotDict = MultiServer.world.getRobots();
        Set<String> robots = robotDict.keySet();
        for (String key:robots) {
            Robot robot = robotDict.get(key);
            System.out.println("Robot name: "+robot.getName()+" position: ("+robot.getPosition().getX()+
                    ","+robot.getPosition().getY()+") Direction: "+robot.getCurrentDirection());
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
