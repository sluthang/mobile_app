package za.co.wethinkcode.robot.server;

import za.co.wethinkcode.robot.server.Commands.Command;
import za.co.wethinkcode.robot.server.Commands.ForwardCommand;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Robot.Robot;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Schedule {
    private final Timer timer;
    private final Robot robot;
    private final String todo;
    private final Server server;
    private final World world;
    private int seconds;

    public Schedule(Server server, World world, String todo, int seconds) throws IOException {
        this.todo = todo;
        this.timer = new Timer();
        this.robot = server.robot;
        this.server = server;
        this.world = world;
        this.seconds = seconds;
        startTask();
    }

    public void startTask() throws IOException {
        timer.schedule(new Task(), seconds * 1000);
        System.out.println("Starttask");

    }

    public void changeRobotState(Robot target, String status) {
        robot.setStatus(status);
    }

    private void repairRobot(Robot target) {
        robot.shields = robot.getMaxShields();
        changeRobotState(robot, "NORMAL");
    }

    private void layMine(Server server, World world) {
        Position oldPos = new Position(server.robot.getPosition().getX(),
                server.robot.getPosition().getY());

        Command forward1 = new ForwardCommand("1");
        forward1.execute(world, server);

        world.getMaze().createMine(oldPos);
        server.robot.setStatus("NORMAL");
    }


    class Task extends TimerTask {
        public void run() {
            System.out.println("started run");
            switch (todo) {
                case "mine":
                    layMine(server, world);
                case "reload":
                    changeRobotState(robot, "NORMAL");
                case "repair":
                    repairRobot(robot);
            }
            timer.cancel(); //Terminate the timer thread
        }
    }
}
