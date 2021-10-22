package za.co.wethinkcode.robot.server.Commands;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Map.Obstacle;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Robot.Robot;
import za.co.wethinkcode.robot.server.Utility.ResponseBuilder;
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

    public String execute(World world, String name) {
        ResponseBuilder responseBuilder = new ResponseBuilder();
        JSONObject data = new JSONObject();
        responseBuilder.add("result", "OK");

        checkObstacles(world.getMaze().getObstacles(), world.VISIBILITY, world, name);
        checkObstacles(world.getMaze().getPits(), world.VISIBILITY, world, name);
        checkObstacles(world.getMaze().getMines(), (int) Math.floor(world.VISIBILITY/2.0), world, name);
        checkRobots(world.getRobots(), world.VISIBILITY, world, name);
        checkForEdge(world, world.VISIBILITY, name);

        data.put("objects", array);
        responseBuilder.addData(data);
        responseBuilder.add("state", world.getRobot(name).getState());
        return responseBuilder.toString();
    }

    private void checkObstacles(Vector<Obstacle> obsList, int visionRange, World world, String name) {
        Robot robot = world.getRobot(name);

        for (Obstacle obstacle: obsList) {

            if (obstacle.blocksPath(robot.getPosition(), new Position(robot.getPosition().getX(), robot.getPosition().getY() + visionRange))) {
                this.array.add(makeJsonObject(obstacle, (obstacle.getBottomLeftY() - robot.getPosition().getY()), "NORTH"));
            } if (obstacle.blocksPath(robot.getPosition(), new Position(robot.getPosition().getX() + visionRange, robot.getPosition().getY()))) {
                this.array.add(makeJsonObject(obstacle, (obstacle.getBottomLeftX() - robot.getPosition().getX()), "EAST"));
            } if (obstacle.blocksPath(robot.getPosition(), new Position(robot.getPosition().getX(), robot.getPosition().getY() - visionRange))) {
                this.array.add(makeJsonObject(obstacle, (robot.getPosition().getY() - obstacle.getBottomLeftY())-(obstacle.getSize()), "SOUTH"));
            } if (obstacle.blocksPath(robot.getPosition(), new Position(robot.getPosition().getX() - visionRange, robot.getPosition().getY()))) {
                this.array.add(makeJsonObject(obstacle, (robot.getPosition().getX() - obstacle.getBottomLeftX())-(obstacle.getSize()), "WEST"));
            }
        }
    }

    private void checkRobots(ConcurrentHashMap<String, Robot> robots, int visionRange, World world, String name ) {
        Set<String> keys = robots.keySet();

        for (String key : keys) {
            Robot robot = robots.get(key);
            if (key.equals(name)) {
                continue;
            }
            if (robot.getPosition().getY() > world.getRobot(name).getPosition().getY() &&
                    robot.getPosition().getY() < world.getRobot(name).getPosition().getY()+visionRange &&
                        robot.getPosition().getX() == world.getRobot(name).getPosition().getX()) {
                this.array.add(makeJsonObject(robot, (robot.getPosition().getY() - world.getRobot(name).getPosition().getY()), "NORTH"));
            } if (robot.getPosition().getX() > world.getRobot(name).getPosition().getX() &&
                    robot.getPosition().getX() < world.getRobot(name).getPosition().getX()+visionRange &&
                        robot.getPosition().getY() == world.getRobot(name).getPosition().getY()) {
                this.array.add(makeJsonObject(robot, (robot.getPosition().getX() - world.getRobot(name).getPosition().getX()), "EAST"));
            } if (robot.getPosition().getY() < world.getRobot(name).getPosition().getY() &&
                    robot.getPosition().getY() > world.getRobot(name).getPosition().getY()-visionRange &&
                        robot.getPosition().getX() == world.getRobot(name).getPosition().getX()) {
                this.array.add(makeJsonObject(robot, (world.getRobot(name).getPosition().getY() - robot.getPosition().getY()), "SOUTH"));
            } if (robot.getPosition().getX() < world.getRobot(name).getPosition().getX() &&
                    robot.getPosition().getX() > world.getRobot(name).getPosition().getX()-visionRange &&
                        robot.getPosition().getY() == world.getRobot(name).getPosition().getY()) {
                this.array.add(makeJsonObject(robot, (world.getRobot(name).getPosition().getX() - robot.getPosition().getX()), "WEST"));
            }
        }
    }

    private void checkForEdge(World world, int visionRange, String name) {


        if (world.getRobot(name).getPosition().getY() + (visionRange-1) >= world.TOP_LEFT.getY() &&
                world.getRobot(name).getPosition().getY() <= world.TOP_LEFT.getY()) {
            this.array.add(makeEdgeJson("NORTH", (world.TOP_LEFT.getY() - world.getRobot(name).getPosition().getY())));
        } if (world.getRobot(name).getPosition().getX() + (visionRange-1) >= world.BOTTOM_RIGHT.getX() &&
                world.getRobot(name).getPosition().getX() <= world.BOTTOM_RIGHT.getX()) {
            this.array.add(makeEdgeJson("EAST", (world.BOTTOM_RIGHT.getX() - world.getRobot(name).getPosition().getX())));
        } if (world.getRobot(name).getPosition().getY() - (visionRange-1) <= world.BOTTOM_RIGHT.getY() &&
                world.getRobot(name).getPosition().getY() >= world.BOTTOM_RIGHT.getY()) {
            this.array.add(makeEdgeJson("SOUTH", (world.getRobot(name).getPosition().getY() - world.BOTTOM_RIGHT.getY())));
        } if (world.getRobot(name).getPosition().getX() - (visionRange-1) <= world.TOP_LEFT.getX() &&
                world.getRobot(name).getPosition().getX() >= world.TOP_LEFT.getX()) {
            this.array.add(makeEdgeJson("WEST", (world.getRobot(name).getPosition().getX() - world.TOP_LEFT.getX())));
        }
    }

    private JSONObject makeEdgeJson(String direction, int distance) {
        JSONObject json = new JSONObject();

        json.put("direction", direction);
        json.put("type", "EDGE");
        json.put("distance", distance+1);

        return json;
    }

    private JSONObject makeJsonObject(Object obstacle, int distance, String direction) {
        JSONObject json = new JSONObject();
        String obstacleType = obstacle.getClass().getSimpleName();

        if (obstacleType.equals("SquareObstacle")) obstacleType = "OBSTACLE";

        json.put("direction", direction);
        json.put("type", obstacleType.toUpperCase());
        json.put("distance", String.valueOf(distance));

        return json;
    }
}
