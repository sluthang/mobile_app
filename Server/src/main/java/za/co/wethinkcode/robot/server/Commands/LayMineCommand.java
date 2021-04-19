package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Robot.Position;
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
            //TODO While laying mines shields should be disabled.
            Command forward1 = new ForwardCommand("1");
            UpdateResponse response = forward1.updatePosition(1, server, world);
            forward1.updatePosition(-1, server, world);

            server.robot.setStatus("SETMINE");
            try {
                new Schedule(server, world, "mine", world.MINE_SET_TIME);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!response.equals(UpdateResponse.SUCCESS)) {
                world.getMaze().hitMine(server.robot.getPosition(), server.robot.getPosition(), server);
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
            throw new IllegalArgumentException("Unsupported command for class");
        }
    }

    private boolean canLay(Server server) {
//        return (server.robot.getShots() == 0);
        return true;
    }
}
