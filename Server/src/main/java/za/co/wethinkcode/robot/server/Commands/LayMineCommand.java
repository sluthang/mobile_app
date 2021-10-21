package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Utility.ResponseBuilder;
import za.co.wethinkcode.robot.server.Utility.Schedule;
import za.co.wethinkcode.robot.server.World;

import java.io.IOException;

@SuppressWarnings("unchecked")
public class LayMineCommand extends Command{


    public LayMineCommand() {
        super("mine");
    }

    /**
     * Checks if the robot can move forward by 1 step, if the robot is obstructed
     * then the mine placed will detonate on the robot that is laying it.
     * Starts the task scheduler for laying the mine on the field.
     * Build the JsonObject to send to the client stating that the task has started.
     * @param world;
//     * @param server;
     */
    @Override
    public String execute(World world, String name) {
        // Checks if the robot is allowed to lay mines.
        ResponseBuilder responseBuilder = new ResponseBuilder();
        if (canLay(world, name)) {
            // Create a forward command to move the robot 1 step ahead after laying mine.
            world.getRobot(name).setStatus("SETMINE");
            world.getRobot(name).setOldShield(world.getRobot(name).getShields());
            world.getRobot(name).setShields(0);
            try {
                new Schedule(world, "mine", world.MINE_SET_TIME, name);
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject data = new JSONObject();
            data.put("message", "Done");
            responseBuilder.addData(data);
            responseBuilder.add("result", "OK");
        } else {
            JSONObject data = new JSONObject();
            data.put("message", "No mines while using a gun.");
            responseBuilder.addData(data);
            responseBuilder.add("result", "ERROR");
        }

        responseBuilder.add("state", world.getRobot(name).getState());
        return responseBuilder.toString();
    }

    private boolean canLay(World world, String name) {
        return (world.getRobot(name).getMaxShots() == 0);
    }
}
