package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Utility.Schedule;
import za.co.wethinkcode.robot.server.Server.Server;
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
     * @param server;
     */
    @Override
    public void execute(World world, Server server) {
        // Checks if the robot is allowed to lay mines.
        if (canLay(server)) {
            // Create a forward command to move the robot 1 step ahead after laying mine.
            server.robot.setStatus("SETMINE");
            server.robot.oldShield = server.robot.shields;
            server.robot.shields = 0;
            try {
                new Schedule(server, world, "mine", world.MINE_SET_TIME);
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject data = new JSONObject();
            data.put("message", "Done");
            server.response.addData(data);
            server.response.add("result", "OK");
        } else {
            JSONObject data = new JSONObject();
            data.put("message", "No mines while using a gun.");
            server.response.addData(data);
            server.response.add("result", "ERROR");
        }
    }

    private boolean canLay(Server server) {
        return (server.robot.getMaxShots() == -1);
    }
}
