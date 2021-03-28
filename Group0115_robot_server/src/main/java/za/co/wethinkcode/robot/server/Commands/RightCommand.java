package za.co.wethinkcode.robot.server.Commands;

import za.co.wethinkcode.robot.server.Robot.Robot;

public class RightCommand extends Command {
    /**
     * Constructor for the right command
     * */
    public RightCommand() {
        super("right");
    }

    /**
     * Overrides the execute command with:
     * setting the current direction, based upon the previous direction, and which it should be facing.
     * */
    @Override
    public boolean execute(Robot target) {
        target.updateDirection(true);
        target.setStatus("Turned right.");
        return true;
    }
}

