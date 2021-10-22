package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Utility.ResponseBuilder;
import za.co.wethinkcode.robot.server.World;

@SuppressWarnings("unchecked")
public class StateCommand extends Command{

    public StateCommand() {
        super("state");
    }

    /**
     * Builds the JsonObject for the state of the robots to send back to client.
     * @param world;
//     * @param server;
     */
    @Override
    public String execute(World world, String name) {
        ResponseBuilder responseBuilder = new ResponseBuilder();
        JSONObject data = new JSONObject();

        if (world.getRobot(name).getActivity()){
            data.put("message", "State");
            responseBuilder.addData(data);
            responseBuilder.add("result", "OK");
            responseBuilder.add("state", world.getRobot(name).getState());

        }
         else {
            data.put("message", "Robot does not exist");
            responseBuilder.addData(data);
            responseBuilder.add("result", "ERROR");

        }
        return responseBuilder.toString();
    }
}
