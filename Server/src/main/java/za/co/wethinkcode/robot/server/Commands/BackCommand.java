package za.co.wethinkcode.robot.server.Commands;

import za.co.wethinkcode.robot.server.Robot.Robot;
import za.co.wethinkcode.robot.server.Robot.UpdateResponse;

public class BackCommand extends Command {
    /**
     * Constructor for back command
     * */
    public BackCommand(String argument) {
        super("back", argument);
    }

    /**
     * Overrides execute with:
     * number of steps and updates the steps on a negative amount so you move back the required
     * number of steps
     * */
    @Override
    public boolean execute(Robot target) {
        int nrSteps = Integer.parseInt(getArgument());
        UpdateResponse response =  target.updatePosition(-nrSteps);
        if (response == UpdateResponse.SUCCESS){
            target.setStatus("Moved back by "+nrSteps+" steps.");
        } else if (response == UpdateResponse.FAILED_OBSTRUCTED){
            target.setStatus("Sorry, Obstacle in my way.");
        } else if (response == UpdateResponse.FAILED_BOTTOMLESS_PIT) {
            target.setStatus("GET REKT! Fell into a pit and died... Monke.");
        } else {
            target.setStatus("Sorry, I cannot go outside my safe zone.");
        }
        return true;
    }
}
