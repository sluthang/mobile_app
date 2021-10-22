package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Utility.ResponseBuilder;
import za.co.wethinkcode.robot.server.Utility.Schedule;
import za.co.wethinkcode.robot.server.World;

import java.io.IOException;


@SuppressWarnings("unchecked")
public class RepairCommand extends Command{


    public RepairCommand() {
        super("repair");
    }

    /**
     * Starts the task of repairing the robots shield.
     * build the JsonObject to send to the client, stating that the repairing has started.
     * @param world;
//     * @param server;
     */
    public String execute(World world, String name) {
        ResponseBuilder responseBuilder  =  new ResponseBuilder();
        try {
            world.getRobot(name).setStatus("REPAIR");
            new Schedule(world, "repair", world.REPAIR_TIME, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject data = new JSONObject();
        data.put("message", "Repair");
        responseBuilder.addData(data);
        responseBuilder.add("result", "OK");
        responseBuilder.add("state", world.getRobot(name).getState());

        return responseBuilder.toString();
    }
}
