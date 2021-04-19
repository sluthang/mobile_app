package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Schedule;
import za.co.wethinkcode.robot.server.Server;
import za.co.wethinkcode.robot.server.World;

import java.io.IOException;

public class ReloadCommand extends Command{

    public ReloadCommand() {
        super("reload");
    }

    @Override
    public void execute(World world, Server server) {
        try {
            server.robot.setStatus("RELOAD");
            new Schedule(server, world, "reload", world.REPAIR_TIME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject data = new JSONObject();
        data.put("message", "Reload");
        server.response.addData(data);
        server.response.add("result", "OK");
    }
}
