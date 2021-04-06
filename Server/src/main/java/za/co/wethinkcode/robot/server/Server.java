package za.co.wethinkcode.robot.server;

import za.co.wethinkcode.robot.server.Commands.Command;
import za.co.wethinkcode.robot.server.Robot.Robot;

import java.io.*;
import java.net.Socket;

public class Server implements Runnable {

    public static final int PORT = MultiServer.config.getPort();
    private final String clientMachine;
    public final BufferedReader in;
    public final PrintStream out;
    public String clientName;

    public Server(Socket socket) throws IOException {
        // Constructor for the class will create the in and out streams.
        clientMachine = socket.getInetAddress().getHostName();
        System.out.println("Connection from " + clientMachine);

        out = new PrintStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        System.out.println("Waiting for client...");
    }

    public void run() {
        try {
            // Default "Play" of the current client.
            Command command;
            boolean shouldContinue = true;
            out.println("What would you like to name your robot?");
            String username = in.readLine();
            this.clientName = username;
            MultiServer.world.addRobot(new Robot(username));
            Robot robot = MultiServer.world.getRobot(username);
            out.println(robot.getStatus() + " " + robot.getName());
            String messageFromClient;
            while((messageFromClient = in.readLine()) != null) {
                System.out.println("Message \"" + messageFromClient + "\" from " + clientMachine);
                try {
                    try {
                        command = Command.create(messageFromClient);
                        shouldContinue = robot.handleCommand(command);
                    } catch (IllegalArgumentException e) {
                        robot.setStatus("Sorry, I did not understand '" + messageFromClient + "'.");
                    }
                    out.println(robot + "____" + robot.getName());
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
