package za.co.wethinkcode.robot.server.Commands;

import za.co.wethinkcode.robot.server.Commands.Command;
import za.co.wethinkcode.robot.server.Position;

public class ForwardCommand extends Command {

    public ForwardCommand(String argument) {
        super("forward", argument);
    }

    @Override
    public boolean execute(Position position) {
        int nrSteps = Integer.parseInt(getArgument());
        return position.updatePosition(nrSteps);
    }
}
