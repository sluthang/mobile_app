package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Utility.ResponseBuilder;
import za.co.wethinkcode.robot.server.Utility.Schedule;
import za.co.wethinkcode.robot.server.Server.Server;
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
     * @param server;
     */
    public String execute(World world, Server server) {
        ResponseBuilder responseBuilder  =  new ResponseBuilder();
        try {
            server.robot.setStatus("REPAIR");
            new Schedule(server, world, "repair", world.REPAIR_TIME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject data = new JSONObject();
        data.put("message", "Repair");
        responseBuilder.addData(data);
        responseBuilder.add("result", "OK");
        responseBuilder.add("state", server.robot.getState());

        return responseBuilder.toString();
    }
}
