package server.Commands;

import za.co.wethinkcode.robot.server.Robot.Robot;

public class LeftCommand extends Command {
    /**
     * Constructor for left command
     * */
    public LeftCommand() {
        super("left");
    }

    /**
     * Overrides the execute command with:
     * setting the current direction, based upon the previous direction, and which it should be facing.
     * */
    @Override
    public boolean execute(Robot target) {
        target.updateDirection(false);
        target.setStatus("Turned left.");
        return true;
    }
}
