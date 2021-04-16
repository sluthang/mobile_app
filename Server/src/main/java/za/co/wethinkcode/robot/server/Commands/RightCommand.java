package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Server;
import za.co.wethinkcode.robot.server.World;

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
    public void execute(World world, Server server) {
        JSONObject data = new JSONObject();

        server.robot.updateDirection(true);

        data.put("message", "Done");
        server.response.addData(data);
        server.response.add("result", "OK");
    }
}

