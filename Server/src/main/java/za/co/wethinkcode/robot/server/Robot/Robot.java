package za.co.wethinkcode.robot.server.Robot;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.MultiServer;
import za.co.wethinkcode.robot.server.ResponseBuilder;
import za.co.wethinkcode.robot.server.Server;
import za.co.wethinkcode.robot.server.World;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;

public class Robot {
    protected final String name;
    protected Direction currentDirection;
    protected Position position;
    private String status;
    private int maxShields;
    private int maxShots;
    public int shields;
    public int jankVarOldShield;
    public int shots;

    /**
     * Constructor for Robot class
     * */
    public Robot(String name) {
        this.name = name;
        this.status = "NORMAL";
        this.currentDirection = Direction.NORTH;
    }

    /**
     * Getter to fetch status
     * */
    public String getStatus() {
        return this.status;
    }

    /**
     * Getter to fetch current direction
     *
     * @return
     * */
    public Direction getCurrentDirection() {
        return this.currentDirection;
    }

    /**
     * Getter to fetch the name of robot
     * */
    public String getName() {
        return name;
    }

    /**
     * An override of the to string method with our own.
     * returns the positions/name/status in proper format
     * */
    @Override
    public String toString() {
        return "[" + this.position.getX() + "," + this.position.getY() + "] "
                + this.name + "> " + this.status;
    }

    /**
     * Setter to set the current direction
     * */
    public void setCurrentDirection(Direction direction) {this.currentDirection = direction;}

    /**
     * Setter to set the current status
     * */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Updates the direction of the robot based on the parameters results, true means it turns right. false means it turns left.
     * @param turnRight: Boolean of which direction it should turn;
     */
    public void updateDirection(boolean turnRight) {
        if (turnRight) {
            switch (getCurrentDirection()){
                case NORTH:
                    this.currentDirection = Direction.EAST;
                    break;
                case EAST:
                    this.currentDirection = Direction.SOUTH;
                    break;
                case SOUTH:
                    this.currentDirection = Direction.WEST;
                    break;
                case WEST:
                    this.currentDirection = Direction.NORTH;
                    break;
            }
        } else {
            switch (getCurrentDirection()) {
                case NORTH:
                    this.currentDirection = Direction.WEST;
                    break;
                case WEST:
                    this.currentDirection = Direction.SOUTH;
                    break;
                case SOUTH:
                    this.currentDirection = Direction.EAST;
                    break;
                case EAST:
                    this.currentDirection = Direction.NORTH;
                    break;
            }
        }
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean blocksPosition(Position position) {
        return this.position.equals(position);
    }

    @SuppressWarnings("unchecked")
    public JSONObject getState(){
        JSONObject state = new JSONObject();
        state.put("position", this.position.getAsList());
        state.put("direction", this.currentDirection.toString());
        state.put("shields", Math.max(this.shields, 0));
        state.put("shots", this.shots);
        state.put("status", isDead());
        return state;
    }

    public String isDead() {
        if (this.shields < 0) {
            this.status = "DEAD";
        }
        return this.status;
    }

    public void kill(World world, Server server, String message) {
        this.shields = -1;

        ResponseBuilder response = new ResponseBuilder();
        JSONObject data = new JSONObject();
        data.put("Message", message);
        response.addData(data);
        response.add("result", "OK");
        response.add("state", getState());

        world.removeRobot(this.name);

        server.out.println(response);
        server.closeThread();
    }

    public void setMaxes(int maxShields, int maxShots) {
        this.maxShields = maxShields;
        this.shields = maxShields;
        this.maxShots = maxShots;
        this.shots = maxShots;
    }

    public int getMaxShields() {
        return this.maxShields;
    }

    public int getMaxShots() { return this.maxShots; }
}