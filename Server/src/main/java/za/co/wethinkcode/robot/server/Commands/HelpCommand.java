package server.Commands;

import za.co.wethinkcode.robot.server.Robot.Robot;

public class HelpCommand extends Command {
    /**
     * Constructor for the help
     * */
    public HelpCommand() {
        super("help");
    }

    /**
     * Overrides the execute command with:
     * setting the current direction, based upon the previous direction, and which it should be facing.
     * */
    @Override
    public boolean execute(Robot target) {
        target.setStatus("I can understand these commands:\n" +
                "OFF  - Shut down robot.\n" +
                "HELP - provide information about commands.\n" +
                "FORWARD - move forward by specified number of steps, e.g. 'FORWARD 10'.\n" +
                "BACK - move back by specified number of steps, e.g. 'BACK 10.\n" +
                "LEFT - change the direction of the robot to the lef.\n" +
                "RIGHT - change the direction of the robot to the right.\n" +
                "SPRINT - move forward n amount of times, decreasing by 1 each time.\n" +
                "REPLAY - replay all the commands that have been already played e.g. 'replay'.\n" +
                "REPLAY N - replay n amount of commands that have been done before e.g. 'replay 5'\n" +
                "REPLAY N-M replay n-m amount of commands that have been done before e.g. 'replay 4-2'\n" +
                "REPLAY REVERSED - replay all the commands in reverse e.g. 'replay reversed'\n" +
                "REPLAY REVERSED N - replay n numbers in reverse e.g. 'replay reversed 2 / replay 2 reversed'.\n" +
                "REPLAY REVERSED N - replay n-m numbers in reverse e.g. 'replay reversed 4-2 / replay 4-2 reversed'.\n" +
                "RESET - resets the world, setting you to middle and facing up (turtle resets the world).\n" +
                "MAZERUN - solve the maze to end wall e.g. 'mazerun'\n" +
                "MAZERUN N - solve the maze to a certain wall e.g. 'mazerun left'\n");
        return true;
    }
}
