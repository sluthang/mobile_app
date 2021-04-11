package za.co.wethinkcode.robot.server.Commands;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Robot.Robot;

public abstract class Command {
    private final String name;
    private String argument;

    public abstract boolean execute(Robot target);

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
     * getter for the name
     * */
    public String getName() {                                                                           //<2>
        return name;
    }

    /**
     * getter for the argument
     * */
    public String getArgument() {
        return this.argument;
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
        String name = instruction.get("command").toString();
        String[] args = ((String[])instruction.get("arguments"));

        if (args.length == 1) {
            switch (args[0]) {
                case "shutdown":
                case "off":
                    return new ShutdownCommand();
                case "left":
                    return new LeftCommand();
                case "right":
                    return new RightCommand();
                default:
                    throw new IllegalArgumentException("Unsupported command: " + instruction);
            }
        } else {
            switch (args[0]) {
                case "forward":
                    return new ForwardCommand(args[1]);
                case "back":
                    return new BackCommand(args[1]);
                default:
                    throw new IllegalArgumentException("Unsupported command: " + instruction);
            }
        }
    }
}

