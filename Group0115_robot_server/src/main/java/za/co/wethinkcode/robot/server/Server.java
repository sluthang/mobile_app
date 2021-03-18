package za.co.wethinkcode.robot.server;

import za.co.wethinkcode.robot.server.Commands.Command;

import java.io.*;
import java.net.*;

public class Server implements Runnable {

    public static final int PORT = 5000;
    private final BufferedReader in;
    private final PrintStream out;
    private final String clientMachine;

    public Server(Socket socket) throws IOException {
        clientMachine = socket.getInetAddress().getHostName();
        System.out.println("Connection from " + clientMachine);

        out = new PrintStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        System.out.println("Waiting for client...");
    }

    public void run() {
        Position position = new Position();
        try {
            String messageFromClient;
            while((messageFromClient = in.readLine()) != null) {
                System.out.println("Message \"" + messageFromClient + "\" from " + clientMachine);
                try {
                    Command command = Command.create(messageFromClient);
                    boolean status = command.execute(position);
                    out.println("new position is (" + position.getX() + "," + position.getY() +
                            ").");
                } catch (IllegalArgumentException e) {
                    out.println("invalid command please try again.");
                }
            }
        } catch(IOException ex) {
            System.out.println("Shutting down single client server");
        } finally {
            closeQuietly();
        }
    }

    private void closeQuietly() {
        try { in.close(); out.close();
        } catch(IOException ex) {}
    }
}
