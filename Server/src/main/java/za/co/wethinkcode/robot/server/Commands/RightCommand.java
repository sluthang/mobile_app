package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Utility.ResponseBuilder;
import za.co.wethinkcode.robot.server.World;

@SuppressWarnings("unchecked")
public class RightCommand extends Command {
    /**
     * Constructor for the right command
     * */
    public RightCommand() {
        super("right");
    }

    /**
     * Overrides the execute command with:
     * setting the current direction, based upon the previous direction, and which it should be facing.
     * */
    @Override
    public String execute(World world, String name) {
        ResponseBuilder responseBuilder =  new ResponseBuilder();
        JSONObject data = new JSONObject();

        world.getRobot(name).updateDirection(true);

        data.put("message", "Done");
        responseBuilder.addData(data);
        responseBuilder.add("result", "OK");
        responseBuilder.add("state", world.getRobot(name).getState());

        return responseBuilder.toString();
    }
}

