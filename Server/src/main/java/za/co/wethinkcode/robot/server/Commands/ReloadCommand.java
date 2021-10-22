package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Utility.ResponseBuilder;
import za.co.wethinkcode.robot.server.Utility.Schedule;
import za.co.wethinkcode.robot.server.World;

import java.io.IOException;

@SuppressWarnings("unchecked")
public class ReloadCommand extends Command{

    public ReloadCommand() {
        super("reload");
    }

    /**
     * Starts the task scheduler for laying a mine on the field.
     * Build the JsonObject to send to the client stating that the reloading has begun.
     * @param world;
//     * @param server;
     */
    @Override
    public String execute(World world, String name) {
        ResponseBuilder responseBuilder  =  new ResponseBuilder();
        try {
            world.getRobot(name).setStatus("RELOAD");
            new Schedule(world, "reload", world.RELOAD_TIME, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject data = new JSONObject();
        data.put("message", "Reload");
        responseBuilder.addData(data);
        responseBuilder.add("result", "OK");
        responseBuilder.add("state", world.getRobot(name).getState());

        return responseBuilder.toString();
    }
}
