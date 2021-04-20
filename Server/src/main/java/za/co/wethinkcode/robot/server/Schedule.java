package za.co.wethinkcode.robot.server;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Commands.Command;
import za.co.wethinkcode.robot.server.Commands.ForwardCommand;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Robot.Robot;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("unchecked")
public class Schedule {
    private ResponseBuilder response;
    private final Timer timer;
    private final Robot robot;
    private final String todo;
    private final Server server;
    private final World world;
    private final int seconds;

    public Schedule(Server server, World world, String todo, int seconds) throws IOException {
        this.response = new ResponseBuilder();
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

    }

    public void changeRobotState(String status) {
        robot.setStatus(status);
    }

    private void repairRobot(Robot target) {
        robot.shields = robot.getMaxShields();
        changeRobotState("NORMAL");

        JSONObject data = new JSONObject();
        data.put("message", "Done");
        response.addData(data);
        response.add("result", "OK");
        response.add("state", robot.getState());
        server.out.println(response.toString());
    }

    private void layMine(Server server, World world) {
        try {Position oldPos = new Position(server.robot.getPosition().getX(),
                server.robot.getPosition().getY());

            if (server.robot.shields == 0){
                world.getMaze().createMine(oldPos);
            }
            server.robot.shields = server.robot.oldShield - server.robot.shields;

            Command forward1 = new ForwardCommand("1");
            forward1.execute(world, server);
            JSONObject data = new JSONObject();
            if (oldPos.equals(server.robot.getPosition())) {
                world.getMaze().hitMine(server.robot.getPosition(), server);
                data.put("message", "Mine");
                if (server.robot.isDead().equals("DEAD")) {
                    server.robot.kill(world, server, "Mine");
                }
            }
            else {
                data.put("message", "Done");
            }
            response.addData(data);

            server.robot.setStatus("NORMAL");

            response.add("result", "OK");
            response.add("state", robot.getState());
            server.out.println(response.toString());
        } catch (Exception e) {
            System.out.println("Robot died before completion");
        }
    }

    private void reload(Server server, World world) {
        robot.shots = robot.getMaxShots();
        changeRobotState("NORMAL");

        JSONObject data = new JSONObject();
        data.put("message", "Done");
        response.addData(data);
        response.add("result", "OK");
        response.add("state", robot.getState());
        server.out.println(response.toString());
    }


    class Task extends TimerTask {
        public void run() {
            switch (todo) {
                case "mine":
                    layMine(server, world);
                    break;
                case "reload":
                    reload(server, world);
                    break;
                case "repair":
                    repairRobot(robot);
                    break;
            }
            timer.cancel(); //Terminate the timer thread
        }
    }
}
