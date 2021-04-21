package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Robot.Robot;
import za.co.wethinkcode.robot.server.Robot.UpdateResponse;
import za.co.wethinkcode.robot.server.Server.MultiServer;
import za.co.wethinkcode.robot.server.Server.Server;
import za.co.wethinkcode.robot.server.Utility.ResponseBuilder;
import za.co.wethinkcode.robot.server.World;

import java.util.Set;

public class FireCommand extends Command{

    public FireCommand() {
        super("fire");
    }

    @Override
    public void execute(World world, Server server) {
        if (server.robot.shots == 0) {
            server.response.add("response", "Error");
            JSONObject data = new JSONObject();
            data.put("message", "You got shot");
            server.response.addData(data);
            return;
        }

        int xStep = 0;
        int yStep = 0;
        switch (server.robot.getCurrentDirection()) {
            case NORTH: yStep = 1; break;
            case EAST: xStep = 1; break;
            case SOUTH: yStep = -1; break;
            case WEST: xStep = -1; break;
        }

        Position robotPos = server.robot.getPosition();
        Position walker = new Position(robotPos.getX(), robotPos.getY());
        Robot target = null;
        int distance = 3 - (server.robot.getMaxShots() - 3);
        int initialDistance = distance;
        Set<String> keys = world.getRobots().keySet();
        Boolean hit = false;
        do {
            walker = new Position(walker.getX() + xStep, walker.getY() + yStep);
            for (String key : keys) {
                if (world.getRobots().get(key).blocksPosition(walker)) {
                    target = world.getRobots().get(key);
                }
            }

            if (target != null) {
                getHit(target.getName());
                hit = true;
                break;
            }
            if (world.maze.blocksPosition(world.getRobots(), walker, server.robotName)
                    != UpdateResponse.SUCCESS) {
                break;
            }
            distance -= 1;
        } while (distance > 0);

        server.response.add("result", "OK");
        JSONObject data = new JSONObject();
        server.robot.shots -= 1;
        if (hit) {
            data.put("message", "Hit");
            data.put("distance", initialDistance - distance + 1);
            data.put("robot", target.getName());
            data.put("state", target.getState());
        }
        else {
            data.put("message", "Miss");
        }

        if (target.isDead().equals("DEAD")) {
            target.kill(world, server, "Mine");
        }

        server.response.addData(data);
    }

    public void getHit(String robotName) {
        for (Server client: MultiServer.clients) {
            if (client.robot.getName().equals(robotName)) {
                client.robot.takeDamage(1);
                client.response = new ResponseBuilder();
                client.response.add("result", "OK");
                JSONObject data = new JSONObject();
                data.put("message", "You got shot");
                client.response.addData(data);
                client.response.add("state", client.robot.getState());
                client.out.println(client.response.toString());
            }
        }
    }
}
