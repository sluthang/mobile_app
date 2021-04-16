package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Server;
import za.co.wethinkcode.robot.server.World;

@SuppressWarnings("unchecked")
public class StateCommand extends Command{

    public StateCommand() {
        super("state");
    }

    @Override
    public void execute(World world, Server server) {
        JSONObject data = new JSONObject();
        data.put("message", "State");
        server.response.addData(data);
    }
}
