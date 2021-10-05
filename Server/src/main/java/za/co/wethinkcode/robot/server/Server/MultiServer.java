package za.co.wethinkcode.robot.server.Server;

import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Utility.ConfigReader;
import za.co.wethinkcode.robot.server.World;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;

public class MultiServer {
    // Static field for the config files contents.
    public static ConfigReader config = new ConfigReader();
    // All client threads will be stored in the Vector array allowing us to use them individually.
    public static Vector<Server> clients = new Vector<>();
    // The loaded world with the map inserted, this will hold all the robots and data.
    public static World world;
    public static Option port;
    public static Option size;
    public static Option obstacle;
    public static CommandLine cmd;
    public static Options options = new Options();
    public static int worldSize;

    public static void main(String[] args) throws IOException, SQLException {
        //Create command line argument options
        {
            port = new Option("p", "port", true, "Server port number to listen on");
            size = new Option("s", "size", true, "Size of the world");
            obstacle = new Option("o", "obstacle", true, "Place an obstacle in the world");
        }
        //Add options
        {
            options.addOption(port);
            options.addOption(size);
            options.addOption(obstacle);
        }

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        try{
            cmd = parser.parse(options, args);

        } catch (ParseException e){
            System.out.println(e.getMessage());
            formatter.printHelp("Available commands", options);
            System.exit(1);
            return;
        }

        worldSize = config.getWorldSize();
        if (cmd.getOptionValue("size") != null){
            worldSize = Integer.parseInt(cmd.getOptionValue("size"));
        }

        System.out.println("World Size: " + worldSize + "x" + worldSize);

        Position BOTTOM_RIGHT = new Position((worldSize/2),(-worldSize/2));
        Position TOP_LEFT = new Position((-worldSize/2),(worldSize/2));

        int port = config.getPort();
        if(cmd.getOptionValue("port") != null){
            port = Integer.parseInt(cmd.getOptionValue("port"));
        }

        //Prints out the servers IP address and port.
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            //socket.connect(InetAddress.getByName("localhost"), 5000);
            System.out.println("IP address: "+socket.getLocalAddress().getHostAddress()+"\nPORT: "+port);
        }

        // Create a new Socket that will be used for the server with the port given from config.
        ServerSocket s = new ServerSocket(port);

        //Checks to see if an argument was provided for determining what maze to use.
        boolean specifiedObstacle = false;
        if (cmd.getOptionValue("obstacle") != null){
            specifiedObstacle = true;
        }

        world = new World(config.getMap(), BOTTOM_RIGHT, TOP_LEFT, getObstaclePosition(cmd.getOptionValue("obstacle")), specifiedObstacle);

        //Print world size
        System.out.println("Obstacles: " + world.getObstacles().size());
        System.out.println("Server running & waiting for client connections.");

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
    public static Position getObstaclePosition(String argument){
        if(argument != null) {
            String[] arguments = argument.split(",");
            int x = Integer.parseInt(arguments[0]);
            int y = Integer.parseInt(arguments[1]);
            return new Position(x,y);
        }
        return null;
    }
}
