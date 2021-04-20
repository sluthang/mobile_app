package za.co.wethinkcode.robot.server.Commands;

import za.co.wethinkcode.robot.server.Server.Server;
import za.co.wethinkcode.robot.server.World;

public class FireCommand extends Command{

    public FireCommand() {
        super("fire");
    }

    @Override
    public void execute(World world, Server server) {
        int xStep = 0;
        int yStep = 0;
        switch (server.robot.getCurrentDirection()) {
            case NORTH: yStep = 1;
            case EAST: xStep = 1;
            case SOUTH: yStep = -1;
            case WEST: xStep = -1;
        }

        
    }
}
