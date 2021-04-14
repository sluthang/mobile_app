package za.co.wethinkcode.robot.server;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import za.co.wethinkcode.robot.server.Commands.Command;
import za.co.wethinkcode.robot.server.Robot.Robot;

import java.io.*;
import java.net.Socket;

public class Server implements Runnable {

    private boolean running;
    private final String clientMachine;
    public final BufferedReader in;
    public final PrintStream out;
    public String clientName;
    public static World world;
    public Robot robot;

    public Server(Socket socket, World world) throws IOException {
        // Constructor for the class will create the in and out streams.
        clientMachine = socket.getInetAddress().getHostName();
        System.out.println("Connection from " + clientMachine);

        out = new PrintStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        System.out.println("Waiting for client...");
        running = true;
        this.world = world;
    }

    public void run() {
        try {
            // Default "Play" of the current client.
            boolean shouldContinue = true;

            String messageFromClient;
            while(running) {
                messageFromClient = in.readLine();
                JSONObject jsonMessage = (JSONObject) JSONValue.parse(messageFromClient);
                System.out.println(messageFromClient);

                this.robot = world.getRobot((String)jsonMessage.get("robot"));
                this.robot.response =  new ResponseBuilder();
                Command command = null;
                this.robot.response = new ResponseBuilder();
                try {
                    command = Command.create(jsonMessage);
                } catch (IllegalArgumentException e) {
                    this.robot.response.add("result", "ERROR");
                    this.robot.response.add("message", "Unsupported command");
                }
                try {
                    world.handleCommand(command, this);
                } catch (IllegalArgumentException e) {
                    this.robot.response.add("result", "ERROR");
                    this.robot.response.add("message", "Could not parse arguments");
                }
                if (this.robot.response.getValue("result") == "") {
                    this.robot.response.add("result", "ERROR");
                    this.robot.response.add("message", "Could not parse arguments");
                }
                this.robot.response.add("status", this.robot.getState());

                out.println(this.robot.response.toString());
            }
        } catch(IOException ex) {
            System.out.println("Shutting down single client server");
        } finally {
            closeQuietly();
        }
    }

    public void closeThread() {
        this.running = false;
    }

    private void closeQuietly() {
        try { in.close(); out.close();
        } catch(IOException ex) {}
    }
}
