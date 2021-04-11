package za.co.wethinkcode.robot.server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import za.co.wethinkcode.robot.server.Commands.Command;
import za.co.wethinkcode.robot.server.Robot.Robot;
import za.co.wethinkcode.robot.server.World.AbstractWorld;
import za.co.wethinkcode.robot.server.World.IWorld;
import za.co.wethinkcode.robot.server.World.World;

import java.io.*;
import java.net.Socket;

public class Server implements Runnable {

    private boolean running;
    private final String clientMachine;
    public final BufferedReader in;
    public final PrintStream out;
    public String clientName;
    public static AbstractWorld world;

    public Server(Socket socket, AbstractWorld world) throws IOException {
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
            //TODO fix me
//            this.clientName = username;

            String messageFromClient;
            while(running) {
                messageFromClient = in.readLine();
                JSONObject jsonMessage = (JSONObject) JSONValue.parse(messageFromClient);
                System.out.println(messageFromClient);

                this.world.response = new ResponseBuilder();
                try {
                    try {
                        Command command = Command.create(jsonMessage);
                    } catch (IllegalArgumentException e) {
//                        robot.setStatus("Sorry, I did not understand '" + messageFromClient + "'.");
                    }

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

    public void closeThread() {
        this.running = false;
    }

    private void closeQuietly() {
        try { in.close(); out.close();
        } catch(IOException ex) {}
    }
}
