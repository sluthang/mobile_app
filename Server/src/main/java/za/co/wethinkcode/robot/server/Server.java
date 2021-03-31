package za.co.wethinkcode.robot.server;

import za.co.wethinkcode.robot.server.Commands.Command;
import za.co.wethinkcode.robot.server.Robot.Robot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Server implements Runnable {

    public static final int PORT = MultiServer.config.getPort();
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
        try {
            Command command;
            boolean shouldContinue = true;
            out.println("What would you like to name your robot?");
            String username = in.readLine();
            MultiServer.world.addRobot(new Robot(username));
            Robot robot = MultiServer.world.getRobot(username);
            out.println(robot.getStatus() + robot.getName());
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
