package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Robot.Robot;
import za.co.wethinkcode.robot.server.Robot.UpdateResponse;
import za.co.wethinkcode.robot.server.Utility.ConfigReader;
import za.co.wethinkcode.robot.server.Utility.ResponseBuilder;
import za.co.wethinkcode.robot.server.World;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class LaunchCommand extends Command{
    JSONArray args;
    public static ConfigReader config = new ConfigReader();

    public LaunchCommand(JSONArray args) {
        super("launch");
        this.args = args;
    }

    /**
     * Method will create a new robot in the world with the given data.
     * The method will first check if the robot does not already exist within the world and if so the method returns
     * an error to the client.
     * If the arguments are valid the method will proceed to find a random open spot in the world and create the new
     * robot at that location in the world and store it in the clients connected thread.
     * @param world of the server.
//     * @param server associated with the client.
     */
    
    @Override
    public String execute(World world, String name) {
        JSONObject data = new JSONObject();
        ResponseBuilder responseBuilder = new ResponseBuilder();
        // Checks if the Robot exists already, then returns error if it is.


        //I could make a new thing (boolean) if the boolean is true than the robot is active else it's not
        // or world.getRobot(name).getActivity() != false
        if (!doesRobotExist(world, name)) {
            world.getRobot(name).setActivity(false);
            data.put("message", "Too many of you in this world");
            responseBuilder.addData(data);
            responseBuilder.add("result", "ERROR");
            return responseBuilder.toString();
        }

        boolean positionSet = false;
        for (int x = - world.TOP_LEFT.getY(); x <= world.TOP_LEFT.getY(); x++) {
            for(int y = - world.TOP_LEFT.getY(); y <= world.TOP_LEFT.getY(); y++){

                if (world.maze.blocksPosition(world.getRobots(), new Position(0, 0), name) == UpdateResponse.SUCCESS){
                    Robot robot  = new Robot(name);
                    robot.setActivity(true);
                    world.addRobot(robot);
                    int maxShield = Math.min(Integer.parseInt(args.get(1).toString()), world.MAX_SHIELDS);
                    int maxShot = Math.min(Integer.parseInt(args.get(2).toString()), world.MAX_SHOTS);
                    world.getRobot(name).setMaxes(maxShield, maxShot);
                    world.getRobot(name).setPosition(new Position(0, 0));
                    positionSet = true;
                    break;
                }
                else if(world.maze.blocksPosition(world.getRobots(), new Position(x, y), name) == UpdateResponse.SUCCESS){
                    Robot robot = new Robot(name);
                    robot.setActivity(true);
                    world.addRobot(robot);
                    int maxShield = Math.min(Integer.parseInt(args.get(1).toString()), world.MAX_SHIELDS);
                    int maxShot = Math.min(Integer.parseInt(args.get(2).toString()), world.MAX_SHOTS);
                    world.getRobot(name).setMaxes(maxShield, maxShot);
                    world.getRobot(name).setPosition(new Position(x, y));
                    positionSet = true;
                    break;
                }
            }
            if(positionSet){
                break;
            }
        }

        if (!positionSet) {
            data.put("message", "No more space in this world");
            responseBuilder.addData(data);
            responseBuilder.add("result", "ERROR");
            return responseBuilder.toString();
        }

        data.put("position", world.getRobot(name).getPosition().getAsList());
        data.put("visibility", config.getVisibility());
        data.put("reload", config.getReloadTime());
        data.put("repair", config.getShieldRechargeTime());
        data.put("mine", config.getMineSetTime());
        data.put("shields", world.getRobot(name).getShields());

        responseBuilder.addData(data);
        responseBuilder.add("result", "OK");
        responseBuilder.add("state", world.getRobot(name).getState());

        return responseBuilder.toString();
    }

    private boolean doesRobotExist(World world, String name) {
        try {
            ConcurrentHashMap<String, Robot> robotDict = world.getRobots();
            Set<String> robots = robotDict.keySet();

            for (String key : robots) {
                Robot currentRobot = robotDict.get(key);
                if (currentRobot.getName().equals(name)) {
                    return false;
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return true;
        }
        return true;
    }
}
