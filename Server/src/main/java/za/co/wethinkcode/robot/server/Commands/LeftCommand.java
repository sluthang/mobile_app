package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Server.Server;
import za.co.wethinkcode.robot.server.World;

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
    public void execute(World world, Server server) {
        JSONObject data = new JSONObject();

        server.robot.updateDirection(false);

        data.put("message", "Done");
        server.response.addData(data);
        server.response.add("result", "OK");
    }
}
