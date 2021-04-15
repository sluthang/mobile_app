package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Robot.UpdateResponse;
import za.co.wethinkcode.robot.server.Server;
import za.co.wethinkcode.robot.server.World;

public class LayMineCommand extends Command{


    public LayMineCommand() {
        super("mine");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(World world, Server server) {
        // Checks if the robot is allowed to lay mines.
        if (canLay(server)) {
            // Creates the mine at the robots location.
            world.getMaze().createMine(new Position(server.robot.getPosition().getX(),
                    server.robot.getPosition().getY()));
            // Create a forward command to move the robot 1 step ahead after laying mine.
            Command forward1 = new ForwardCommand("1");
            UpdateResponse response = forward1.updatePosition(1, server, world);

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
        } else {
            throw new IllegalArgumentException("Unsupported command for class");
        }
    }

    private boolean canLay(Server server) {
//        return (server.robot.getShots() == 0);
        return true;
    }
}
