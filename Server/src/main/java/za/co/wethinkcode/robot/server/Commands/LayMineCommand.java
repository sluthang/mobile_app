package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Robot.UpdateResponse;
import za.co.wethinkcode.robot.server.Schedule;
import za.co.wethinkcode.robot.server.Server;
import za.co.wethinkcode.robot.server.World;

import java.io.IOException;

@SuppressWarnings("unchecked")
public class LayMineCommand extends Command{


    public LayMineCommand() {
        super("mine");
    }

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

            Position currentPosition = server.robot.getPosition();
            currentPosition = new Position(currentPosition.getX(), currentPosition.getY());
            Command forward = new ForwardCommand("1");
            forward.execute(world, server);
            if (currentPosition.equals(server.robot.getPosition())) {
                world.getMaze().hitMine(server.robot.getPosition(), server);
                JSONObject data = new JSONObject();
                data.put("message", "Mine");
                server.response.addData(data);
            } else {
                JSONObject data = new JSONObject();
                data.put("message", "Done");
                server.response.addData(data);
            }
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
