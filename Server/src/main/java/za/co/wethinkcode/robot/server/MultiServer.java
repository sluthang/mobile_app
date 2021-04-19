package za.co.wethinkcode.robot.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class MultiServer {
    // Static field for the config files contents.
    public static ConfigReader config = new ConfigReader();
    // All client threads will be stored in the Vector array allowing us to use them individually.
    static Vector<Server> clients = new Vector<>();
    // The loaded world with the map inserted, this will hold all the robots and data.
    public static World world = new World();

    public static void main(String[] args) throws IOException {
        // Create a new Socket that will be used for the server with the port given from config.
        ServerSocket s = new ServerSocket(config.getPort());
        System.out.println("Server running & waiting for client connections.");

        // Create our Thread for using the System.in of the server, this will run along side the server.
        Thread output = new Thread(new ServerManagement());
        output.start();
        while(output.isAlive()) {
            try {
                // Create a new thread once a connection is made from a client. Client is added to Vector array.
                Socket socket = s.accept();
                System.out.println("Connection: " + socket);

                Server r = new Server(socket, world);
                Thread task = new Thread(r);
                task.start();
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
