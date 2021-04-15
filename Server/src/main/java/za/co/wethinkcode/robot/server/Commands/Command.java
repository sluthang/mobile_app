package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Robot.Direction;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Robot.UpdateResponse;
import za.co.wethinkcode.robot.server.Server;
import za.co.wethinkcode.robot.server.World;

public abstract class Command {
    private final String name;
    private String argument;
    protected World world;

    public abstract void execute(World world, Server server);

    /**
     * constructor for command with no arguments
     * */
    public Command(String name){
        this.name = name.trim().toLowerCase();
        this.argument = "";
    }

    /**
     * constructor for command with arguments
     * */
    public Command(String name, String argument) {
        this(name);
        this.argument = argument.trim();
    }

    /**
     * getter for the argument
     * */
    public String getArgument() {
        return this.argument;
    }

    public String getName(){
        return this.name;
    }

    /**
     * create command that does:
     * creates args, based on the arguments passed through.
     * checks if the args is just 1 (so like 'help' instead of 'forward 1'), and runs those commands.
     * else if does the commands and passes through their arguments.
     * if the arugment is illegal or unsupported then it will throw back an exception
     *
     * @param instruction*/
    public static Command create(JSONObject instruction) {
        String command = instruction.get("command").toString();
        String[] args = ((String[])instruction.get("arguments"));

        switch (command) {
            case "forward":
                return new ForwardCommand(args[1]);
            case "back":
                return new ForwardCommand("-" + args[1]);
            case "turn":
                switch (args[0]) {
                    case "left":
                        return new LeftCommand();
                    case "right":
                        return new RightCommand();
                }
            case "mine":
                return new LayMineCommand();
            case "launch": new LaunchCommand();
            default:
                throw new IllegalArgumentException("Unsupported command: " + instruction);
        }
    }

    /**
     * Checks the old position of the robot against the new positions of the robot. In 3 ways, first it checks if their is
     * a obstacle in the way, secondly it checks if the new position is actually allowed (if yes it moves),
     * lastly it returns a failed out of bounds otherwise.
     * @param nrSteps: the number of steps the robot will move;
     * @return: an UpdateResponse of what the result of moving the robot is.
     * */
    public UpdateResponse updatePosition(int nrSteps, Server server) {
        Position currentPosition = server.robot.getPosition();
        Direction currentDirection = server.robot.getCurrentDirection();

        int oldX = currentPosition.getX();
        int oldY = currentPosition.getY();
        int newX = currentPosition.getX();
        int newY = currentPosition.getY();

        switch (currentDirection) {
            case UP:
                newY = newY + nrSteps;
                break;
            case DOWN:
                newY = newY - nrSteps;
                break;
            case LEFT:
                newX = newX - nrSteps;
                break;
            case RIGHT:
                newX = newX + nrSteps;
                break;
        }

        Position oldPosition = new Position(oldX, oldY);
        Position newPosition = new Position(newX, newY);

        UpdateResponse response = world.maze.blocksPath(oldPosition, newPosition, world.getRobots());
        if (response != UpdateResponse.SUCCESS) return response;

        response = world.isNewPositionAllowed(oldPosition, newPosition);
        if (response != UpdateResponse.SUCCESS) return response;

        server.robot.setPosition(newPosition);
        return UpdateResponse.SUCCESS;
    }
}

