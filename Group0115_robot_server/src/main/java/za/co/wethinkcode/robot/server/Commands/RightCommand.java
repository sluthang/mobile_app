package za.co.wethinkcode.robot.server.Commands;

import za.co.wethinkcode.robot.server.Position;

public class RightCommand extends Command{

    public RightCommand(String name) {
        super(name);
    }

    public boolean execute(Position position) {
        switch (position.getDirection()) {
            case "NORTH":
                position.direction = "EAST";
                break;
            case "EAST":
                position.direction = "SOUTH";
                break;
            case "SOUTH":
                position.direction = "WEST";
                break;
            case "WEST":
                position.direction = "NORTH";
                break;
        }
        return true;
    }
}
