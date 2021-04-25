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
        if (server.robot.getShots() == 0) {
            server.response.add("response", "Error");
            JSONObject data = new JSONObject();
            data.put("message", "Please reload.");
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
        Server targetServer = null;
        do {
            walker = new Position(walker.getX() + xStep, walker.getY() + yStep);
            for (String key : keys) {
                if (world.getRobots().get(key).blocksPosition(walker)) {
                    target = world.getRobots().get(key);
                }
            }

            if (target != null) {
                targetServer = getHit(target.getName());
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
        server.robot.setShots(server.robot.getShots() - 1);
        if (hit) {
            data.put("message", "Hit");
            data.put("distance", initialDistance - distance + 1);
            data.put("robot", target.getName());
            data.put("state", target.getState());
        }
        else {
            data.put("message", "Miss");
        }

        if (target != null) {
            if (target.isDead().equals("DEAD")) {
                target.kill(world, targetServer, "Shot");
            }
        }

        server.response.addData(data);
    }

    public Server getHit(String robotName) {
        Server out = null;
        for (Server client: MultiServer.clients) {
            if (client.robot.getName().equals(robotName)) {
                out = client;
                client.robot.takeDamage(1);
                if (client.robot.getShields() == -1) return client;
                client.response = new ResponseBuilder();
                client.response.add("result", "OK");
                JSONObject data = new JSONObject();
                data.put("message", "Shot");
                client.response.addData(data);
                client.response.add("state", client.robot.getState());
                client.out.println(client.response.toString());
                break;
            }
        }
        return out;
    }
}
