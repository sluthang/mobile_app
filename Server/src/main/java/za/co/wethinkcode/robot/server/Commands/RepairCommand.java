package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Schedule;
import za.co.wethinkcode.robot.server.Server;
import za.co.wethinkcode.robot.server.World;

import java.io.IOException;

@SuppressWarnings("unchecked")
public class RepairCommand extends Command{


    public RepairCommand() {
        super("repair");
    }

    public void execute(World world, Server server) {
        try {
            server.robot.setStatus("REPAIR");
            new Schedule(server, world, "repair", 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject data = new JSONObject();
        data.put("message", "Repair");
        server.response.addData(data);
    }
}
