package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Utility.Schedule;
import za.co.wethinkcode.robot.server.Server.Server;
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
     * @param server;
     */
    @Override
    public void execute(World world, Server server) {
        try {
            server.robot.setStatus("RELOAD");
            new Schedule(server, world, "reload", world.RELOAD_TIME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject data = new JSONObject();
        data.put("message", "Reload");
        server.response.addData(data);
        server.response.add("result", "OK");
    }
}
