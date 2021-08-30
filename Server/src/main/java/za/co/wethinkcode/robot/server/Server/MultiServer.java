package za.co.wethinkcode.robot.server.Server;

import za.co.wethinkcode.robot.server.Utility.ConfigReader;
import za.co.wethinkcode.robot.server.World;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class MultiServer {
    // Static field for the config files contents.
    public static ConfigReader config = new ConfigReader();
    // All client threads will be stored in the Vector array allowing us to use them individually.
    public static Vector<Server> clients = new Vector<>();
    // The loaded world with the map inserted, this will hold all the robots and data.
    public static World world;

    public static void main(String[] args) throws IOException {
        //Prints out the servers IP address and port.
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            System.out.println("IP address: "+socket.getLocalAddress().getHostAddress()+"\nPORT: "+config.getPort());
        }
        // Create a new Socket that will be used for the server with the port given from config.
        ServerSocket s = new ServerSocket(config.getPort());
        System.out.println("Server running & waiting for client connections.");

        //Checks to see if an argument was provided for determining what maze to use.
        if(args.length != 0){
            world = new World(args[0]);
        }else{
            world = new World("Random");
        }
        // Create our Thread for using the System.in of the server, this will run along side the server.
        Thread output = new Thread(new ServerManagement(world));
        output.start();
        while(output.isAlive()) {
            try {
                // Create a new thread once a connection is made from a client. Client is added to Vector array.
                Socket socket = s.accept();
                System.out.println(ServerManagement.ANSI_GREEN+"Connection: " + ServerManagement.ANSI_RESET + socket);

                Server r = new Server(socket, world);
                Thread task = new Thread(r);
                task.start();
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
