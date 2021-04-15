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
    public String robotName;
    public ResponseBuilder response;

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
                JSONObject jsonMessage = (JSONObject)JSONValue.parse(messageFromClient);
                System.out.println(messageFromClient);

                this.robotName = (String)jsonMessage.get("robot");
                this.robot = world.getRobot(this.robotName);
                this.response = new ResponseBuilder();

                Command command = Command.create(jsonMessage);
                world.handleCommand(command, this);

                this.response.add("state", this.robot.getState());
                out.println(this.response.toString());
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
