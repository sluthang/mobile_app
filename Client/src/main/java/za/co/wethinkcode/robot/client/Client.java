package za.co.wethinkcode.robot.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static String ip;
    private static int port = 0;

    public static void main(String[] args) {
        // Client will ask the user for the IP address and port of the server, then set these values.
        setIpAndPort();
        try (
                // Client will attempt to establish a connection to the server.
                Socket socket = new Socket(ip, port);
                PrintStream out = new PrintStream(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()))
        )
        {
            // New thread made for listening for input from the server. Runs constantly and in sync.
            Thread listener = new Thread(new ListenerThread(in));
            listener.start();

            // New thread for sending output to the server. Runs in sync and constantly.
            Thread output = new Thread(new OutputThread(out));
            output.start();

            // Loop to keep the Client running. (must add a close for this.)
            while (true) {
                if (!listener.isAlive()) {
                    System.exit(69);
                }
            }
        } catch (IOException e) {
            System.out.println("Error has occurred please check ip and port, then try again.");
        }
    }

    /**
     * Method will prompt the user for the IP and PORT of the server user is attempting to connect to.
     */
    public static void setIpAndPort() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Ip address please: ");
        Client.ip = sc.nextLine();
        while (Client.port == 0) {
            try {
                System.out.print("Port: ");
                Client.port = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                Client.port = 0;
            }
        }
    }
}
