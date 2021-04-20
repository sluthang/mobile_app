package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.MultiServer;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Robot.Robot;
import za.co.wethinkcode.robot.server.Robot.UpdateResponse;
import za.co.wethinkcode.robot.server.Server;
import za.co.wethinkcode.robot.server.World;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class LaunchCommand extends Command{
    JSONArray args;
    public LaunchCommand(JSONArray args) {
        super("launch");
        this.args = args;
    }

    @Override
    public void execute(World world, Server server) {
        JSONObject data = new JSONObject();
        if (!doesRobotExist(world, server)) {
            server.robotName = null;
            data.put("message", "Too many of you in this world");
            server.response.addData(data);
            server.response.add("result", "ERROR");
            return;
        } else if (server.robot == null){
            server.robot = new Robot(server.robotName);
            world.addRobot(server.robot);
            int maxShield = Math.min(Integer.parseInt(args.get(1).toString()), MultiServer.config.getMaxShieldStrength());
            int maxShot = Integer.parseInt(args.get(2).toString());
            server.robot.setMaxes(maxShield, maxShot);
        }

        Random random = new Random();

        boolean positionSet = false;
        for (int i = 0; i < 1000; i++) {
//            int x = random.nextInt(world.BOTTOM_RIGHT.getX() - world.TOP_LEFT.getX()) - world.BOTTOM_RIGHT.getX();
//            int y = random.nextInt(world.TOP_LEFT.getY() - world.BOTTOM_RIGHT.getY()) - world.TOP_LEFT.getY();
            int x = 0;
            int y = 0;

            if (world.maze.blocksPosition(world.getRobots(), new Position(x, y), server.robotName) == UpdateResponse.SUCCESS){
                server.robot.setPosition(new Position(x, y));
                positionSet = true;
                break;
            }
        }
        if (!positionSet) {
            data.put("message", "No more space in this world");
            server.response.addData(data);
            server.response.add("result", "ERROR");
            return;
        }

        data.put("position", server.robot.getPosition().getAsList());
        data.put("visibility", MultiServer.config.getVisibility());
        data.put("reload", MultiServer.config.getReloadTime());
        data.put("repair", MultiServer.config.getShieldRechargeTime());
        data.put("mine", MultiServer.config.getMineSetTime());
        data.put("shields", server.robot.shields);

        server.response.addData(data);
        server.response.add("result", "OK");
    }

    private boolean doesRobotExist(World world, Server server) {
        try {
            ConcurrentHashMap<String, Robot> robotDict = world.getRobots();
            Set<String> robots = robotDict.keySet();

            for (String key : robots) {
                Robot currentRobot = robotDict.get(key);
                if (currentRobot.getName().equals(server.robotName)) {
                    return false;
                }
            }
        } catch (NullPointerException e) {
            return true;
        }
        return true;
    }
}
