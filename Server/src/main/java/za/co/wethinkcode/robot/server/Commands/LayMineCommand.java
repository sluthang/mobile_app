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
        if (canLay(server)) {
            world.getMaze().createMine(new Position(server.robot.getPosition().getX(),
                    server.robot.getPosition().getY()));
            Command forward1 = new ForwardCommand("1");
            UpdateResponse response = forward1.updatePosition(1, server);

            if (!response.equals(UpdateResponse.SUCCESS)) {
                world.getMaze().hitMine(server.robot.getPosition(), server.robot.getPosition(), server);
                JSONObject data = new JSONObject();
                data.put("message", "Mine");
                server.robot.response.addData(data);
            } else {
                JSONObject data = new JSONObject();
                data.put("message", "Done");
                server.robot.response.addData(data);
            }
        } else {
            throw new IllegalArgumentException("Unsupported command for class");
        }
    }

    private boolean canLay(Server server) {
        return (server.robot.getShots() == 0);
    }
}
