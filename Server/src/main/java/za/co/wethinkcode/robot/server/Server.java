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
    public final Socket socket;
    public final BufferedReader in;
    public final PrintStream out;
    public static World world;
    public Robot robot;
    public String robotName;
    public ResponseBuilder response;

    public Server(Socket socket, World world) throws IOException {
        // Constructor for the class will create the in and out streams.
        this.socket = socket;
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
        printClientMessage(jsonMessage);

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
        printClientMessage(jsonMessage);

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
        if (robot.isDead().equals("DEAD")) {
            robot.kill(world,this, "Dead");
        }
        this.response.add("state", this.robot.getState());
        out.println(this.response.toString());
    }

    public void closeThread() {
        this.running = false;
        closeQuietly();
    }

    @SuppressWarnings("CatchMayIgnoreException")
    private void closeQuietly() {
        try {
            socket.close();
            in.close(); out.close();

            MultiServer.clients.remove(MultiServer.clients.indexOf(this));
            System.out.println(this.robotName + ": has left the server");

            if (this.robot != null) {
                world.removeRobot(this.robotName);
            }
        } catch(IOException ex) {
            ex.printStackTrace();
        } catch (IndexOutOfBoundsException ex) {}
    }

    private void printClientMessage(JSONObject message) {
        System.out.println("\u001b[33;1m"+"Message from  : "+"\u001B[0m"+ this.robotName+"\n"+
                "\n" +
                "\u001B[32;1m"+"Robot name: "+"\u001B[0m"+ message.get("robot")+"\n"+
                "\u001B[35;1m"+"Argument  : "+"\u001B[0m"+ message.get("arguments")+"\n"+
                "\u001B[34;1m"+"Command   : "+"\u001B[0m"+ message.get("command")+"\n");
    }
}
