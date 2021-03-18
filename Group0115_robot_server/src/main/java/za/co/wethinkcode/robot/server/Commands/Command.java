package za.co.wethinkcode.robot.server.Commands;

import za.co.wethinkcode.robot.server.Position;

public abstract class Command {
    private final String name;
    private String argument;

    public abstract boolean execute(Position position);

    public Command(String name){
        this.name = name.trim().toLowerCase();
        this.argument = "";
    }

    public Command(String name, String argument) {
        this(name);
        this.argument = argument.trim();
    }

    public String getName() {
        return name;
    }

    public String getArgument() {
        return this.argument;
    }

    public static Command create(String instruction) {
        String[] args = instruction.toLowerCase().trim().split(" ");
        switch (args[0]){
            case "right":
                return new RightCommand(args[0]);
            case "left":
                return new LeftCommand(args[0]);
            case "forward":
                if (args.length == 1) throw new IllegalArgumentException("Unsupported command: " + instruction);
                return new ForwardCommand(args[1]);
            case "back":
                if (args.length == 1) throw new IllegalArgumentException("Unsupported command: " + instruction);
                return new BackCommand(args[1]);
            default:
                throw new IllegalArgumentException("Unsupported command: " + instruction);
        }
    }
}

