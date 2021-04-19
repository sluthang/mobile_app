package za.co.wethinkcode.robot.server.Robot;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.ResponseBuilder;

public class Robot {
    protected final String name;
    protected Direction currentDirection;
    protected Position position;
    private String status;
    private int maxShields;
    private int maxShots;
    public int shields;
    public int shots;

    /**
     * Constructor for Robot class
     * */
    public Robot(String name) {
        this.name = name;
        this.status = "NORMAL";
        this.currentDirection = Direction.UP;
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
                case UP:
                    this.currentDirection = Direction.RIGHT;
                    break;
                case RIGHT:
                    this.currentDirection = Direction.DOWN;
                    break;
                case DOWN:
                    this.currentDirection = Direction.LEFT;
                    break;
                case LEFT:
                    this.currentDirection = Direction.UP;
                    break;
            }
        } else {
            switch (getCurrentDirection()) {
                case UP:
                    this.currentDirection = Direction.LEFT;
                    break;
                case LEFT:
                    this.currentDirection = Direction.DOWN;
                    break;
                case DOWN:
                    this.currentDirection = Direction.RIGHT;
                    break;
                case RIGHT:
                    this.currentDirection = Direction.UP;
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
        state.put("shields", this.shields);
        state.put("shots", this.shots);
        state.put("status", this.status);
        return state;
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