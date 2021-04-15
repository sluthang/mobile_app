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

        String message = "";
        if (response == UpdateResponse.SUCCESS){
            message = "Done";
        } else if (response == UpdateResponse.FAILED_OBSTRUCTED){
            message = "Obstructed";
        } else if (response == UpdateResponse.FAILED_BOTTOMLESS_PIT) {
            message = "Fell";
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

    /**
     * Checks the old position of the robot against the new positions of the robot. In 3 ways, first it checks if their is
     * a obstacle in the way, secondly it checks if the new position is actually allowed (if yes it moves),
     * lastly it returns a failed out of bounds otherwise.
     * @param nrSteps: the number of steps the robot will move;
     * @return: an UpdateResponse of what the result of moving the robot is.
     * */
    public UpdateResponse updatePosition(int nrSteps, Server server, World world) {
        Position currentPosition = server.robot.getPosition();
        Direction currentDirection = server.robot.getCurrentDirection();

        int oldX = currentPosition.getX();
        int oldY = currentPosition.getY();
        int newX = currentPosition.getX();
        int newY = currentPosition.getY();

        switch (currentDirection) {
            case UP:
                newY = newY + nrSteps;
                break;
            case DOWN:
                newY = newY - nrSteps;
                break;
            case LEFT:
                newX = newX - nrSteps;
                break;
            case RIGHT:
                newX = newX + nrSteps;
                break;
        }
        System.out.println("test1");
        Position oldPosition = new Position(oldX, oldY);
        Position newPosition = new Position(newX, newY);

        UpdateResponse response = world.maze.blocksPath(oldPosition, newPosition, world.getRobots());
        if (response != UpdateResponse.SUCCESS) return response;

        response = world.isInWorld(oldPosition, newPosition);
        if (response != UpdateResponse.SUCCESS) return response;

        server.robot.setPosition(newPosition);
        return UpdateResponse.SUCCESS;
    }
}

