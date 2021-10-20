package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Utility.ResponseBuilder;
import za.co.wethinkcode.robot.server.World;

@SuppressWarnings("unchecked")
public class LeftCommand extends Command {
    /**
     * Constructor for left command
     * */
    public LeftCommand() {
        super("left");
    }

    /**
     * Overrides the execute command with:
     * setting the current direction, based upon the previous direction, and which it should be facing.
     * */
    @Override
    public String execute(World world, String name) {
        JSONObject data = new JSONObject();
        ResponseBuilder responseBuilder = new ResponseBuilder();

        world.getRobot(name).updateDirection(false);

        data.put("message", "Done");
        responseBuilder.addData(data);
        responseBuilder.add("result", "OK");
        responseBuilder.add("state", world.getRobot(name).getState());

        return responseBuilder.toString();
    }
}