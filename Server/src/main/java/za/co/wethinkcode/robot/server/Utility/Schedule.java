package za.co.wethinkcode.robot.server.Utility;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Commands.Command;
import za.co.wethinkcode.robot.server.Commands.ForwardCommand;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Robot.Robot;
import za.co.wethinkcode.robot.server.Server.Server;
import za.co.wethinkcode.robot.server.World;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("unchecked")
public class Schedule {
    private final ResponseBuilder response;
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

    /**
     * Starts the new task to be created.
     */
    public void startTask() {
        timer.schedule(new Task(), seconds * 1000L);
    }

    /**
     * Changes the status of the robot while a task is being created or completed.
     * @param status to change.
     */
    public void changeRobotState(String status) {
        robot.setStatus(status);
    }

    /**
     * Method will after a given seconds delay perform a task that repairs the robot.
     * Robots shields will set to the maximum that were set at launch and state will be set to NORMAL.
     *A JsonObject is constructed to let the user know that the task has completed and is sent to the user with new the new state.
     */
    private void repairRobot() {
        robot.setShields(robot.getMaxShields());
        changeRobotState("NORMAL");

        JSONObject data = new JSONObject();
        data.put("message", "Done");
        response.addData(data);
        response.add("result", "OK");
        response.add("state", robot.getState());
        server.out.println(response);
    }

    /**
     * Method will after a given seconds delay perform a task that repairs the robot.
     * Robot will move forward by one place and place a mine on the old location that he was on.
     * Status of robot is set to NORMAL and a JsonObject is built to send to the user with new state.
     * @param server to perform task on.
     * @param world to update mines list.
     */
    private void layMine(Server server, World world) {
        try {Position oldPos = new Position(server.robot.getPosition().getX(),
                server.robot.getPosition().getY());

            if (server.robot.getShields() == 0){
                world.getMaze().createMine(oldPos);
            }
            server.robot.setShields(server.robot.getOldShield() - server.robot.getShields());

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
            server.out.println(response);
        } catch (Exception e) {
            System.out.println("Robot died before completion");
        }
    }

    /**
     * Method will after a given seconds delay perform a task that reloads the robots ammo.
     * Robots shots will set to the maximum that were set at launch and state will be set to NORMAL.
     * A JsonObject is constructed to let the user know that the task has completed and is sent to the user with new the new state.
     * @param server to issue commands to.
     */
    private void reload(Server server) {
        robot.setShots(robot.getMaxShots());
        changeRobotState("NORMAL");

        JSONObject data = new JSONObject();
        data.put("message", "Done");
        response.addData(data);
        response.add("result", "OK");
        response.add("state", robot.getState());
        server.out.println(response);
    }

    /**
     * Method will choose which task needs to be scheduled to run.
     */
    class Task extends TimerTask {
        public void run() {
            switch (todo) {
                case "mine":
                    layMine(server, world);
                    break;
                case "reload":
                    reload(server);
                    break;
                case "repair":
                    repairRobot();
                    break;
            }
            timer.cancel(); //Terminate the timer thread
        }
    }
}
