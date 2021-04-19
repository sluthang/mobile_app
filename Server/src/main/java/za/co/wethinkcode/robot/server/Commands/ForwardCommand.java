package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Robot.Direction;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Robot.UpdateResponse;
import za.co.wethinkcode.robot.server.Server;
import za.co.wethinkcode.robot.server.World;

public class ForwardCommand extends Command {
    private World currentWorld;

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
    public void execute(World world, Server server) {
        JSONObject data = new JSONObject();
        int nrSteps = 0;
        try {
            String argument = getArgument();
            if (argument.contains("--")){
                argument = argument.substring(2, argument.length() - 1);
            }
            nrSteps = Integer.parseInt(argument);
        }catch (NumberFormatException e) {
            data.put("message", "Could not parse arguments");
            server.response.addData(data);
            server.response.add("result", "ERROR");
            return;
        }

        UpdateResponse response =  updatePosition(nrSteps, server, world);

        //TODO set position to whatever it hits
        String message = "";
        if (response == UpdateResponse.SUCCESS){
            message = "Done";
        } else if (response == UpdateResponse.FAILED_OBSTRUCTED){
            message = "Obstructed";
        } else if (response == UpdateResponse.FAILED_BOTTOMLESS_PIT) {
            message = "Fell";
            server.robot.kill(world, server, "Fell");
            //TODO kill robot
        } else if (response == UpdateResponse.FAILED_HIT_MINE) {
            message = "Mine";
            //TODO mine things
        } else if (response == UpdateResponse.FAILED_OUTSIDE_WORLD){
            message = "Obstructed";
        }

        data.put("message", message);
        server.response.addData(data);
        server.response.add("result", "OK");
    }
}

