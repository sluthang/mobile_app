package za.co.wethinkcode.robot.server;

import za.co.wethinkcode.robot.server.Map.Maze;
import za.co.wethinkcode.robot.server.Map.RandomMaze;
import za.co.wethinkcode.robot.server.World.IWorld;
import za.co.wethinkcode.robot.server.World.World;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class MultiServer {
    // Static field for the config files contents.
    public static ConfigReader config = new ConfigReader();
    // The map that the world will be using.
    public static Maze maze = new RandomMaze();
    // The loaded world with the map inserted, this will hold all the robots and data.
    public static IWorld world = new World(maze);
    // All client threads will be stored in the Vector array allowing us to use them individually.
    static Vector<Server> clients = new Vector<>();

    public static void main(String[] args) throws IOException {
        world.showObstacles();
        // Create a new Socket that will be used for the server with the port given from config.
        ServerSocket s = new ServerSocket(Server.PORT);
        System.out.println("Server running & waiting for client connections.");

        // Create our Thread for using the System.in of the server, this will run along side the server.
        Thread output = new Thread(new ServerManagement());
        output.start();

        while(output.isAlive()) {
            try {
                // Create a new thread once a connection is made from a client. Client is added to Vector array.
                Socket socket = s.accept();
                System.out.println("Connection: " + socket);

                Server r = new Server(socket);
                Thread task = new Thread(r);
                clients.add(r);
                task.start();
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
