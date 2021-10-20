package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Robot.Direction;
import za.co.wethinkcode.robot.server.Robot.UpdateResponse;
import za.co.wethinkcode.robot.server.Server.Server;
import za.co.wethinkcode.robot.server.Utility.ResponseBuilder;
import za.co.wethinkcode.robot.server.World;

@SuppressWarnings({"unchecked", "unused"})
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
    public String execute(World world, String name) {
        JSONObject data = new JSONObject();
        ResponseBuilder responseBuilder = new ResponseBuilder();
        int nrSteps;
        try {
            String argument = getArgument();
            if (argument.contains("--")){
                argument = argument.substring(2, argument.length() - 1);
            }
            nrSteps = Integer.parseInt(argument);
        }catch (NumberFormatException e) {
            data.put("message", "Could not parse arguments");
            responseBuilder.addData(data);
            responseBuilder.add("result", "ERROR");
            responseBuilder.add("state", world.getRobot(name).getState());
            return responseBuilder.toString();
        }

        UpdateResponse response = UpdateResponse.SUCCESS;
        int step = 1;
        if (nrSteps < 0) step = -1;
        while (nrSteps != 0 && response == UpdateResponse.SUCCESS) {
            response =  updatePosition(step, world, name);
            nrSteps -= step;
        }

        String message = "";
        if (response == UpdateResponse.SUCCESS){
            message = "Done";
        } else if (response == UpdateResponse.FAILED_OBSTRUCTED){
            message = "Obstructed";
        } else if (response == UpdateResponse.FAILED_BOTTOMLESS_PIT) {
            message = "Fell";
            world.kill(world, message, name);
        } else if (response == UpdateResponse.FAILED_HIT_MINE) {
            message = "Mine";
            world.maze.hitMine(world.getRobot(name).getPosition(), world, name);
            if (world.getRobot(name).isDead().equals("DEAD")) {
                world.kill(world, message, name);
            }
        } else if (response == UpdateResponse.FAILED_OUTSIDE_WORLD){
            message = setMessageAtEdge(world.getRobot(name).getCurrentDirection());
        }

        data.put("message", message);
        responseBuilder.addData(data);
        responseBuilder.add("result", "OK");
        responseBuilder.add("state", world.getRobot(name).getState());
        return responseBuilder.toString();
    }

    /**
     * Sets the message for if the robot is at the edge of the world.
     * @param direction
     * @return String
     */
    public String setMessageAtEdge(Direction direction){
        String message;
        switch (direction){
            case NORTH:
                message = "At the NORTH edge";
                break;
            case EAST:
                message = "At the EAST edge";
                break;
            case WEST:
                message = "At the WEST edge";
                break;
            default:
                message = "At the SOUTH edge";
        }
        return message;
    }

    public String deathResponse(World world, Server server, String message, String name) {
        //Sets the robots shields to below 0.
        world.getRobot(name).setShields(-1);

        //Builds a Json response to send to client.
        ResponseBuilder response = new ResponseBuilder();
        JSONObject data = new JSONObject();
        data.put("message", message);
        response.addData(data);
        response.add("result", "OK");
        response.add("state", world.getRobot(name).getState());

        return response.toString();
    }
}

