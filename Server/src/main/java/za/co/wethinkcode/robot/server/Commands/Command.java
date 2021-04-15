package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
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
            case "launch": new LaunchCommand(args);
            default:
                throw new IllegalArgumentException("Unsupported command: " + instruction);
        }
    }
}

