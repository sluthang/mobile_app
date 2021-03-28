package za.co.wethinkcode.robot.server.Commands;

import za.co.wethinkcode.robot.server.Robot.UpdateResponse;
import za.co.wethinkcode.robot.server.Robot.Robot;

public class ForwardCommand extends Command {
    /**
     * Constructor for forward command
     * */
    public ForwardCommand(String argument) {
        super("forward", argument);
    }

    /**
     * Overrides execute with:
     * number of steps and updates the steps on a positive amount so you move back the required
     * number of steps
     * */
    @Override
    public boolean execute(Robot target) {
        int nrSteps = Integer.parseInt(getArgument());
        UpdateResponse response =  target.updatePosition(nrSteps);
        if (response == UpdateResponse.SUCCESS){
            target.setStatus("Moved forward by "+nrSteps+" steps.");
        } else if (response == UpdateResponse.FAILED_OBSTRUCTED){
            target.setStatus("Sorry, Obstacle in my way.");
        } else {
            target.setStatus("Sorry, I cannot go outside my safe zone.");
        }
        return true;
    }
}

