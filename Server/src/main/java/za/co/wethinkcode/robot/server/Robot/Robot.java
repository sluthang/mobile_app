package za.co.wethinkcode.robot.server.Robot;

import za.co.wethinkcode.robot.server.Commands.Command;
import za.co.wethinkcode.robot.server.MultiServer;

public class Robot {
    protected final Position TOP_LEFT = new Position((-MultiServer.config.getWidth()/2),(MultiServer.config.getHeight()/2));
    protected final Position BOTTOM_RIGHT = new Position((MultiServer.config.getWidth()/2),(-MultiServer.config.getHeight()/2));
    private final String name;
    protected Direction currentDirection;
    protected Position position;
    private String status;

    /**
     * Constructor for Robot class
     * */
    public Robot(String name) {
        this.name = name;
        this.status = "Ready";
        this.position = new Position(0, 0);
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
     * executes the command given by the user.
     * */
    public boolean handleCommand(Command command) {
        return command.execute(this);
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

    /**
     * Checks the old position of the robot against the new positions of the robot. In 3 ways, first it checks if their is
     * a obstacle in the way, secondly it checks if the new position is actually allowed (if yes it moves),
     * lastly it returns a failed out of bounds otherwise.
     * @param nrSteps: the number of steps the robot will move;
     * @return: an UpdateResponse of what the result of moving the robot is.
     * */
    public UpdateResponse updatePosition(int nrSteps) {
        int oldX = this.position.getX();
        int oldY = this.position.getY();
        int newX = this.position.getX();
        int newY = this.position.getY();

        switch (this.currentDirection) {
            case UP:
                newY = newY + nrSteps;
                break;
            case DOWN:
                newY = newY - nrSteps;
                break;
            case LEFT:
                newX = newX - nrSteps;
                break;
            case RIGHT:
                newX = newX + nrSteps;
                break;
        }

        Position oldPosition = new Position(oldX, oldY);
        Position newPosition = new Position(newX, newY);

        if (MultiServer.maze.blocksPath(oldPosition, newPosition) == UpdateResponse.FAILED_BOTTOMLESS_PIT) {
            return UpdateResponse.FAILED_BOTTOMLESS_PIT;
        } else if (MultiServer.maze.blocksPath(oldPosition, newPosition) == UpdateResponse.FAILED_OBSTRUCTED) {
            return UpdateResponse.FAILED_OBSTRUCTED;
        } else if (MultiServer.maze.blocksPath(oldPosition, newPosition) == UpdateResponse.FAILED_HIT_MINE) {
            this.position = MultiServer.maze.hitMine(oldPosition, newPosition);
            return UpdateResponse.FAILED_HIT_MINE;
            // TODO: reduce health. send response for hitting mine.
        } else if (isNewPositionAllowed(newPosition)) {
            this.position = newPosition;
            return UpdateResponse.SUCCESS;
        }
        return UpdateResponse.FAILED_OUTSIDE_WORLD;
    }

    /**
     * This function checks if the position is allowed.
     * @param position: takes in the position.
     * @return: boolean true if allowed, false if not allowed.
     * */
    public boolean isNewPositionAllowed(Position position) {
        return position.isIn(TOP_LEFT, BOTTOM_RIGHT);
    }

    public Position getPosition() {
        return position;
    }

    public boolean blocksPosition(Position a) {
        return (a.getX() >= position.getX() && a.getX() <= (position.getX())) &&
                (a.getY() >= position.getY() && a.getY() <= (position.getY()));
    }
}