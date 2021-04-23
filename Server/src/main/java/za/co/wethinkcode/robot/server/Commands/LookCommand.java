package za.co.wethinkcode.robot.server.Commands;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Map.Obstacle;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Robot.Robot;
import za.co.wethinkcode.robot.server.Server.Server;
import za.co.wethinkcode.robot.server.World;

import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class LookCommand extends Command{
    JSONArray array;

    public LookCommand() {
        super("look");
        array = new JSONArray();
    }

    public void execute(World world, Server server) {
        JSONObject data = new JSONObject();
        server.response.add("result", "OK");

        checkObstacles(server, world.getMaze().getObstacles(), world.VISIBILITY);
        checkObstacles(server, world.getMaze().getPits(), world.VISIBILITY);
        checkObstacles(server, world.getMaze().getMines(), (int) Math.floor(world.VISIBILITY/2.0));
        checkRobots(server, world.getRobots(), world.VISIBILITY);
        checkForEdge(server, world, world.VISIBILITY);

        data.put("objects", array);
        server.response.addData(data);
        server.response.add("state", server.robot.getState());
    }

    private void checkObstacles(Server server, Vector<Obstacle> obsList, int visionRange) {
        Robot robot = server.robot;

        for (Obstacle obstacle: obsList) {

            if (obstacle.blocksPath(robot.getPosition(), new Position(robot.getPosition().getX(), robot.getPosition().getY() + visionRange))) {
                this.array.add(makeJsonObject(obstacle, (obstacle.getBottomLeftY() - robot.getPosition().getY()), "NORTH"));
            } if (obstacle.blocksPath(robot.getPosition(), new Position(robot.getPosition().getX() + visionRange, robot.getPosition().getY()))) {
                this.array.add(makeJsonObject(obstacle, (obstacle.getBottomLeftX() - robot.getPosition().getX()), "EAST"));
            } if (obstacle.blocksPath(robot.getPosition(), new Position(robot.getPosition().getX(), robot.getPosition().getY() - visionRange))) {
                this.array.add(makeJsonObject(obstacle, (robot.getPosition().getY() - obstacle.getBottomLeftY())-obstacle.getSize(), "SOUTH"));
            } if (obstacle.blocksPath(robot.getPosition(), new Position(robot.getPosition().getX() - visionRange, robot.getPosition().getY()))) {
                this.array.add(makeJsonObject(obstacle, (robot.getPosition().getY() - obstacle.getBottomLeftY())-obstacle.getSize(), "WEST"));
            }
        }
    }

    private void checkRobots(Server server, ConcurrentHashMap<String, Robot> robots, int visionRange) {
        Set<String> keys = robots.keySet();

        for (String key : keys) {
            Robot robot = robots.get(key);
            if (key.equals(server.robotName)) {
                continue;
            }
            if (robot.getPosition().getY() > server.robot.getPosition().getY() &&
                    robot.getPosition().getY() < server.robot.getPosition().getY()+visionRange &&
                        robot.getPosition().getX() == server.robot.getPosition().getX()) {
                this.array.add(makeJsonObject(robot, (robot.getPosition().getY() - server.robot.getPosition().getY()), "NORTH"));
            } if (robot.getPosition().getX() > server.robot.getPosition().getX() &&
                    robot.getPosition().getX() < server.robot.getPosition().getX()+visionRange &&
                        robot.getPosition().getY() == server.robot.getPosition().getY()) {
                this.array.add(makeJsonObject(robot, (robot.getPosition().getX() - server.robot.getPosition().getX()), "EAST"));
            } if (robot.getPosition().getY() < server.robot.getPosition().getY() &&
                    robot.getPosition().getY() > server.robot.getPosition().getY()-visionRange &&
                        robot.getPosition().getX() == server.robot.getPosition().getX()) {
                this.array.add(makeJsonObject(robot, (server.robot.getPosition().getY() - robot.getPosition().getY()), "SOUTH"));
            } if (robot.getPosition().getX() < server.robot.getPosition().getX() &&
                    robot.getPosition().getX() > server.robot.getPosition().getX()-visionRange &&
                        robot.getPosition().getY() == server.robot.getPosition().getY()) {
                this.array.add(makeJsonObject(robot, (server.robot.getPosition().getX() - robot.getPosition().getX()), "WEST"));
            }
        }
    }

    private void checkForEdge(Server server, World world, int visionRange) {


        if (server.robot.getPosition().getY() + (visionRange-1) >= world.TOP_LEFT.getY() &&
                server.robot.getPosition().getY() <= world.TOP_LEFT.getY()) {
            this.array.add(makeEdgeJson("NORTH", (world.TOP_LEFT.getY() - server.robot.getPosition().getY())));
        } if (server.robot.getPosition().getX() + (visionRange-1) >= world.BOTTOM_RIGHT.getX() &&
                server.robot.getPosition().getX() <= world.BOTTOM_RIGHT.getX()) {
            this.array.add(makeEdgeJson("EAST", (world.BOTTOM_RIGHT.getX() - server.robot.getPosition().getX())));
        } if (server.robot.getPosition().getY() - (visionRange-1) <= world.BOTTOM_RIGHT.getY() &&
                server.robot.getPosition().getY() >= world.BOTTOM_RIGHT.getY()) {
            this.array.add(makeEdgeJson("SOUTH", (server.robot.getPosition().getY() - world.BOTTOM_RIGHT.getY())));
        } if (server.robot.getPosition().getX() - (visionRange-1) <= world.TOP_LEFT.getX() &&
                server.robot.getPosition().getX() >= world.TOP_LEFT.getX()) {
            this.array.add(makeEdgeJson("WEST", (server.robot.getPosition().getX() - world.TOP_LEFT.getX())));
        }
    }

    private JSONObject makeEdgeJson(String direction, int distance) {
        JSONObject json = new JSONObject();

        json.put("direction", direction);
        json.put("type", "EDGE");
        json.put("distance", String.valueOf(distance+1));

        return json;
    }

    private JSONObject makeJsonObject(Object obstacle, int distance, String direction) {
        JSONObject json = new JSONObject();

        json.put("direction", direction);
        json.put("type", obstacle.getClass().getSimpleName());
        json.put("distance", String.valueOf(distance));

        return json;
    }
}
