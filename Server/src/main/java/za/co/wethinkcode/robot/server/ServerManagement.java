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

            // if /message is used a direct message is sent to a user. the username must be included.
            if (serverMessage.contains("/message")) {
                // /message <username> <message>, will fix later.
                for (Server client : MultiServer.clients) {
                    if (serverMessage.contains(client.clientName)) {
                        client.out.println(serverMessage);
                        client.out.flush();
                        System.out.println("Message sent to: " +
                                " "+ serverMessage + " " + client.clientName);
                        break;
                    }
                }
                // If /command is used it will issue server side methods.
            } else if (serverMessage.contains("/command")) {
                //execute server commands that will alter the world.
                if (serverMessage.contains("quit")) {
                    quitServer();
                } else if (serverMessage.contains("robots")) {
                    listRobots();
                } else if (serverMessage.contains("purge")) {
                    purgeUser("hal");
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
        int count = 0;
        for (Server client:MultiServer.clients) {
            if (client.clientName.equals(username)) {
                System.out.println(MultiServer.clients.toString());
                client.closeThread();
                MultiServer.clients.remove(count);
                MultiServer.world.removeRobot(username);
                System.out.println(MultiServer.clients.toString());
            }
            count++;
        }
    }
}
