package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Robot.Direction;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Robot.UpdateResponse;
import za.co.wethinkcode.robot.server.World;

@SuppressWarnings("unused")
public abstract class Command {
    private final String name;
    private String argument;

    public abstract String execute(World world, String name  );

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
     * if the argument is illegal or unsupported then it will throw back an exception
     *
     * @param instruction;
     * */
    public static Command create(JSONObject instruction) {
        String command = instruction.get("command").toString();
        JSONArray args = (JSONArray) instruction.get("arguments");

        switch (command) {
            case "state":
                return new StateCommand();
            case "forward":
                return new ForwardCommand(args.get(0).toString());
            case "back":
                return new ForwardCommand("-" + args.get(0).toString());
            case "turn":
                switch (args.get(0).toString()) {
                    case "left":
                        return new LeftCommand();
                    case "right":
                        return new RightCommand();
                }
            case "look":
                return new LookCommand();
            case "mine":
                return new LayMineCommand();
            case "repair":
                return new RepairCommand();
            case "launch":
                return new LaunchCommand(args);
            case "reload":
                return new ReloadCommand();
            case "fire":
                return new FireCommand();
            default:
                throw new IllegalArgumentException("Unsupported command: " );
        }
    }

    /**
     * Checks the old position of the robot against the new positions of the robot. In 3 ways, first it checks if their is
     * a obstacle in the way, secondly it checks if the new position is actually allowed (if yes it moves),
     * lastly it returns a failed out of bounds otherwise.
     * @param nrSteps: the number of steps the robot will move;
     * @return: an UpdateResponse of what the result of moving the robot is.
     * */
    public UpdateResponse updatePosition(int nrSteps, World world, String name) {
        Position currentPosition = world.getRobot(name).getPosition();
        Direction currentDirection = world.getRobot(name).getCurrentDirection();

        int oldX = currentPosition.getX();
        int oldY = currentPosition.getY();
        int newX = currentPosition.getX();
        int newY = currentPosition.getY();

        switch (currentDirection) {
            case NORTH:
                newY = newY + nrSteps;
                break;
            case SOUTH:
                newY = newY - nrSteps;
                break;
            case WEST:
                newX = newX - nrSteps;
                break;
            case EAST:
                newX = newX + nrSteps;
                break;
        }
        Position oldPosition = new Position(oldX, oldY);
        Position newPosition = new Position(newX, newY);

        UpdateResponse response;
        if (Math.abs(nrSteps) == 1) {
            response = world.maze.blocksPosition(world.getRobots(), newPosition, name);
        }
        else {
            response = world.maze.blocksPath(oldPosition, newPosition, world.getRobots(), name);
            //this broke, don't touch
        }

        if (response == UpdateResponse.FAILED_HIT_MINE) {
            world.getRobot(name).setPosition(newPosition);
            return response;
        }
        if (response != UpdateResponse.SUCCESS) return response;

        response = world.isInWorld(oldPosition, newPosition);
        if (response != UpdateResponse.SUCCESS) return response;

        world.getRobot(name).setPosition(newPosition);
        return UpdateResponse.SUCCESS;
    }
}

