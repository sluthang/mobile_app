package za.co.wethinkcode.robot.server.Robot;

import org.json.simple.JSONObject;
import za.co.wethinkcode.robot.server.Utility.ResponseBuilder;
import za.co.wethinkcode.robot.server.Server.Server;
import za.co.wethinkcode.robot.server.World;

@SuppressWarnings({"unused", "unchecked"})
public class Robot {
    private final String name;
    private Direction currentDirection;
    private Position position;
    private String status;
    private int maxShields;
    private int maxShots;
    private int shields;
    private int oldShield;
    private int shots;

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
     * @return currentDirection.
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
    public void setCurrentDirection(Direction direction) {
        this.currentDirection = direction;
    }

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
            turnRight();
        } else {
            turnLeft();
        }
    }

    /**
     * Updates the current direction the robot is facing when a turn right command is given.
     */
    private void turnRight(){
        switch (getCurrentDirection()) {
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
    }

    /**
     * Updates the current direction the robot is facing when a turn left command is given.
     */
    private void turnLeft(){
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

    /**
     * Checks if the position given matches the current position of the robot.
     * @param position to be compared.
     * @return true if blocked.
     */
    public boolean blocksPosition(Position position) {
        return this.position.equals(position);
    }

    /**
     * Creates a JsonObject that is filled with the robots current state.
     * The fields that are included are position, direction, shields, shots and status.
     * @return JsonObject with state.
     */
    @SuppressWarnings("unchecked")
    public JSONObject getState(){
        JSONObject state = new JSONObject();
        state.put("position", this.position.getAsList());
        state.put("direction", this.currentDirection.toString());
        state.put("shields", Math.max(this.shields, 0));
        state.put("shots", Math.max(this.shots, 0));
        state.put("status", isDead());
        return state;
    }

    /**
     * Method will check if the robot is dead.
     * If the robots shield is below 0 then the robot is considered dead.
     * @return true/false.
     */
    public String isDead() {
        if (this.shields < 0) {
            this.status = "DEAD";
        }
        return this.status;
    }

    /**
     * Method will kill the robot and purge user that is associated with that robot.
     * @param world;
     * @param server;
     * @param message;
     */
    public void kill(World world, Server server, String message) {
        //Sets the robots shields to below 0.
        this.shields = -1;

        //Builds a Json response to send to client.
        ResponseBuilder response = new ResponseBuilder();
        JSONObject data = new JSONObject();
        data.put("message", message);
        response.addData(data);
        response.add("result", "OK");
        response.add("state", getState());

        System.out.println("\033[31;1m"+"User: "+this.name+" has been killed!\n"+
                "Reason for death: "+"\033[0m"+message);

        //Sends response to client and closes their thread.
        server.out.println(response);
        server.closeThread();
    }

    /**
     * Sets the max for shields and shots.
     * @param maxShields;
     * @param maxShots;
     */
    public void setMaxes(int maxShields, int maxShots) {
        this.maxShields = maxShields;
        this.shields = maxShields;
        this.maxShots = maxShots;
        this.shots = maxShots;
    }

    /**
     * Reduces the shield of the robot by the given int value.
     * @param damage;
     */
    public void takeDamage(int damage) {
        this.shields -= damage;
    }

    public int getMaxShields() {
        return this.maxShields;
    }

    public int getMaxShots() { return this.maxShots; }

    public void setShots(int shots) {
        this.shots = shots;
    }

    public void setOldShield(int oldShield) {
        this.oldShield = oldShield;
    }

    public void setMaxShots(int maxShots) {
        this.maxShots = maxShots;
    }

    public void setMaxShields(int maxShield) {
        this.maxShields = maxShield;
    }

    public void setShields(int shield) {
        this.shields = shield;
    }

    public int getShots() {
        return this.shots;
    }

    public int getOldShield() {
        return this.oldShield;
    }

    public int getShields() {
        return this.shields;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}