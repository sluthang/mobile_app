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

@SuppressWarnings("unchecked")
public class FireCommand extends Command{

    public FireCommand() {
        super("fire");
    }

    /**
     * 1. Check if the robot firing is able to fire a gun, or has no ammo. Returns a response to reload.
     * 2. Checks in which direction the robot is firing his gun.
     * 3. loops through list of robots, checks if robots at the allowed firing range are present.
     * 4. if a robot is found a response is build for the client with the robot hits state.
     * 5. getHit is called and the robot hit is passed through as an argument.
     * @param world object currently used.
     * @param server of the client calling the fire command.
     */
    @Override
    public void execute(World world, Server server) {
        // If robot has no shots left a message is sent to the client to reload.
        if (server.robot.getShots() == 0) {
            server.response.add("response", "Error");
            JSONObject data = new JSONObject();
            data.put("message", "Please reload.");
            server.response.addData(data);
            return;
        }

        // Check which direction the robot is firing in.
        int xStep = 0;
        int yStep = 0;
        switch (server.robot.getCurrentDirection()) {
            case NORTH: yStep = 1; break;
            case EAST: xStep = 1; break;
            case SOUTH: yStep = -1; break;
            case WEST: xStep = -1; break;
        }

        // Create the relevant vars to be used for checking.
        Position robotPos = server.robot.getPosition();
        Position walker = new Position(robotPos.getX(), robotPos.getY());
        Robot target = null;
        int distance = 3 - (server.robot.getMaxShots() - 3);
        int initialDistance = distance;
        Set<String> keys = world.getRobots().keySet();
        boolean hit = false;
        Server targetServer = null;

        // Loop through the robots and execute if a robot is hit or missed.
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

    /**
     * Loops through the client list to find the client of the robot that was hit, a response will be built
     * for that robot to inform them of being shot, if the robot is killed from the shot the method returns. The robot
     * that was killed will be sent a dead message if this is the case otherwise the method will build and send the
     * shot response to the user.
     * @param robotName of client shot.
     * @return Server client that was shot.
     */
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
