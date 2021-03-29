package za.co.wethinkcode.robot.server.Commands;

import za.co.wethinkcode.robot.server.Robot.Robot;

public class ShutdownCommand extends Command {
    /**
     * Constructor for shutdown
     * */
    public ShutdownCommand() {
        super("off");
    }

    /**
     * Overrides the execute command, for shutdown. setting status to ShutDown, and returning false to end programs
     * */
    @Override
    public boolean execute(Robot target) {
        target.setStatus("Shutting down...");
        return false;
    }
}

