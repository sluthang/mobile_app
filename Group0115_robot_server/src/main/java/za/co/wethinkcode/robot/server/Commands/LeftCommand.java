package za.co.wethinkcode.robot.server.Commands;

import za.co.wethinkcode.robot.server.Position;

public class LeftCommand extends Command{

    public LeftCommand(String name) {
        super(name);
    }

    public boolean execute(Position position) {
        switch (position.getDirection()) {
            case "NORTH":
                position.direction = "WEST";
                break;
            case "EAST":
                position.direction = "NORTH";
                break;
            case "SOUTH":
                position.direction = "EAST";
                break;
            case "WEST":
                position.direction = "SOUTH";
                break;
        }
        return true;
    }
}

