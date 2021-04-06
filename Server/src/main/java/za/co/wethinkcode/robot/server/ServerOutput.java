package za.co.wethinkcode.robot.server;

import java.io.PrintStream;
import java.util.Scanner;

public class ServerOutput implements Runnable {
    private final Scanner sc;

    public ServerOutput() {
        this.sc = new Scanner(System.in);
    }

    public void run() {
        while (true) {
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
                System.out.println("Issues command.");
            }
        }
    }
}
