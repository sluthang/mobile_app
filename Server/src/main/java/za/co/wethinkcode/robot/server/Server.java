package za.co.wethinkcode.robot.server;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import za.co.wethinkcode.robot.server.Commands.Command;
import za.co.wethinkcode.robot.server.Robot.Robot;

import java.io.*;
import java.net.Socket;

@SuppressWarnings({"unchecked", "RedundantCollectionOperation"})
public class Server implements Runnable {

    private boolean running;
    private final String clientMachine;
    public final BufferedReader in;
    public final PrintStream out;
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
            while(robot == null) {
                messageFromClient = in.readLine();
                handleMessageBeforeLaunch(messageFromClient);
            }

            MultiServer.clients.add(this);
            while(running) {
                messageFromClient = in.readLine();
                handleClientMessage(messageFromClient);
            }
        } catch(IOException | NullPointerException ex) {
            ex.printStackTrace();
            System.out.println("Shutting down single client server");
        } finally {
            closeQuietly();
        }
    }

    private void handleMessageBeforeLaunch(String messageFromClient) {
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

    private void handleClientMessage(String messageFromClient) {
        JSONObject jsonMessage = (JSONObject)JSONValue.parse(messageFromClient);
        System.out.println(messageFromClient);

        this.robotName = (String)jsonMessage.get("robot");
        this.robot = world.getRobot(this.robotName);
        this.response = new ResponseBuilder();

        if(robot.getStatus().equals("NORMAL")) {
            Command command = Command.create(jsonMessage);
            world.handleCommand(command, this);
        } else {
            jsonMessage.put("command", "state");
            Command command = Command.create(jsonMessage);
            world.handleCommand(command, this);
        }
        //TODO if the sheild is -1 return state of DEAD.
        this.response.add("state", this.robot.getState());
        out.println(this.response.toString());
    }

    public void closeThread() {
        this.running = false;
    }

    private void closeQuietly() {
        try {
            //TODO Before closing, send a dead state to the user, this should close the client.
            in.close(); out.close();
            System.out.println(Thread.currentThread().getName() + ": has left the server");
            if (this.robot != null) {
                world.removeRobot(this.robotName);
                MultiServer.clients.remove(MultiServer.clients.indexOf(this));
            }
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}
