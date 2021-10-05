package za.co.wethinkcode.robot.server;

import za.co.wethinkcode.robot.server.Commands.Command;
import za.co.wethinkcode.robot.server.Map.*;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Robot.Robot;
import za.co.wethinkcode.robot.server.Robot.UpdateResponse;
import za.co.wethinkcode.robot.server.Server.MultiServer;
import za.co.wethinkcode.robot.server.Server.Server;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class World{
    //Hashmap of robots currently in play.
    private final ConcurrentHashMap<String, Robot> robots = new ConcurrentHashMap<>();
    //Sets the maps Bottom-right and top-left positions set in the config file.
    public Position BOTTOM_RIGHT;
    public Position TOP_LEFT;
    // The map that the world will be using.
    public Maze maze;
    //Values that were received from the config file.
    public final int MAX_SHOTS = MultiServer.config.getMaxShots();
    public final int MAX_SHIELDS = MultiServer.config.getMaxShieldStrength();
    public final int RELOAD_TIME = MultiServer.config.getReloadTime();
    public final int REPAIR_TIME = MultiServer.config.getReloadTime();
    public final int VISIBILITY = MultiServer.config.getVisibility();
    public final int MINE_SET_TIME = MultiServer.config.getMineSetTime();

    public World(String maze,Position BOTTOM_RIGHT, Position TOP_LEFT, Position obstaclePosition, boolean specified) {
        this.TOP_LEFT = TOP_LEFT;
        this.BOTTOM_RIGHT = BOTTOM_RIGHT;
        if(maze.equalsIgnoreCase("nofreespacemaze")){
            this.maze = new NoFreeSpaceMaze(TOP_LEFT, BOTTOM_RIGHT);
        }
        else if(maze.equalsIgnoreCase("emptymaze")){
            this.maze = new EmptyMaze(obstaclePosition, specified);
        } else{
            this.maze = new RandomMaze(TOP_LEFT, BOTTOM_RIGHT, obstaclePosition, specified);
        }
    }

    /**
     * getter to get the maze.
     * */
    public Maze getMaze() {
        return maze;
    }

    /**
     * Return the list of SquareObstacles inside the map being used currently.
     * @return Vector list of obstacle objects.
     */
    public Vector<Obstacle> getObstacles() {
        return this.maze.getObstacles();
    }

    /**
     * Remove the robot at the given key.
     * If a robot is dead or a client is disconnected this method will need to called to remove them from play.
     * @param name of the robot to be removed
     */
    public void removeRobot(String name) {
        this.robots.remove(name);
    }

    /**
     * Method will call the execute method on the given command.
     * @param command to be executed.
     * @param server that the command will issue data.
     */
    public void handleCommand(Command command, Server server) {
        command.execute(this, server);
    }

    /**
     * Adds the newly created robot to the hashmap of robots on play.
     * The Robot objects name will be used as the key.
     * @param target to be added to list.
     */
    public void addRobot(Robot target) {
        this.robots.put(target.getName(), target);
    }

    /**
     * Returns the robot from the hashmap with the given String as the key.
     * @param name of robot
     * @return Robot object.
     */
    public Robot getRobot(String name){
        return this.robots.get(name);
    }

    /**
     * Returns the hashmap containing all the robots currently in play.
     * Key: robot name, Value: Robot object
     * @return hashmap of robots
     */
    public ConcurrentHashMap<String, Robot> getRobots() {
        return robots;
    }

    /**
     * Shows the obstacles that are inside of the obstacle list.
     * printing them in the terminal at the positions of the obstacles.
     * */
    public void showObstacles() {
        maze.getObstacles();
        for (Obstacle maze : maze.getObstacles()) {
            System.out.println("Wall- At position "+ maze.getBottomLeftX()+ "," +maze.getBottomLeftY()+ " (to "+
                    (maze.getBottomLeftX() + 4) + "," + (maze.getBottomLeftY() + 4) + ")");
        }

        for (Obstacle pit : maze.getPits()) {
            System.out.println("Pit- At position "+ pit.getBottomLeftX()+ "," +pit.getBottomLeftY()+ " (to "+
                    (pit.getBottomLeftX() + 4) + "," + (pit.getBottomLeftY() + 4) + ")");
        }

        for (Obstacle mine : maze.getMines()) {
            System.out.println("Mine- At position "+ mine.getBottomLeftX()+ "," +mine.getBottomLeftY()+ " (to "+
                    (mine.getBottomLeftX()) + "," + (mine.getBottomLeftY()) + ")");
        }
    }

    /**
     * This function checks if the position is allowed.
     * @return: boolean true if allowed, false if not allowed.
     * */
    public UpdateResponse isInWorld(Position oldPosition, Position newPosition) {
        if (newPosition.isIn(TOP_LEFT, BOTTOM_RIGHT)) return UpdateResponse.SUCCESS;
        else return UpdateResponse.FAILED_OUTSIDE_WORLD;
    }
}
