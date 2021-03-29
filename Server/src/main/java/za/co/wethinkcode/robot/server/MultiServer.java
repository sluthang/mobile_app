package za.co.wethinkcode.robot.server;

import za.co.wethinkcode.robot.server.Map.DesignedMaze;
import za.co.wethinkcode.robot.server.Map.Maze;
import za.co.wethinkcode.robot.server.World.IWorld;
import za.co.wethinkcode.robot.server.World.World;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {
    public static Maze maze = new DesignedMaze();
    public static IWorld world = new World(maze);


    public static void main(String[] args) throws ClassNotFoundException, IOException {
        ServerSocket s = new ServerSocket(Server.PORT);
        System.out.println("Server running & waiting for client connections.");
        while(true) {
            try {
                Socket socket = s.accept();
                System.out.println("Connection: " + socket);

                Runnable r = new Server(socket);
                Thread task = new Thread(r);
                task.start();
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
