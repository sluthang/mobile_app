package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Robot.Robot;
import za.co.wethinkcode.robot.server.Server;
import za.co.wethinkcode.robot.server.World;

import java.util.Arrays;
import java.util.List;

public class LaunchCommand extends Command{
    List<String> args;
    public LaunchCommand(String[] args) {
        super("launch");
        this.args = Arrays.asList(args);
    }

    @Override
    public void execute(World world, Server server) {
        JSONObject data = new JSONObject();
        Robot robot = new Robot();

        server.robot.response.addData(data);
    }
}
