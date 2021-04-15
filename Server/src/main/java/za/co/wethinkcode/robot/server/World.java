package za.co.wethinkcode.robot.server;

import za.co.wethinkcode.robot.server.Commands.Command;
import za.co.wethinkcode.robot.server.Map.BaseMaze;
import za.co.wethinkcode.robot.server.Map.EmptyMaze;
import za.co.wethinkcode.robot.server.Map.Maze;
import za.co.wethinkcode.robot.server.Map.Obstacle;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Robot.Robot;
import za.co.wethinkcode.robot.server.Robot.UpdateResponse;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class World{
    public final Position TOP_LEFT = new Position((-MultiServer.config.getWidth()/2),(MultiServer.config.getHeight()/2));
    public final Position BOTTOM_RIGHT = new Position((MultiServer.config.getWidth()/2),(-MultiServer.config.getHeight()/2));
    protected static final Position CENTRE = new Position(0,0);
    protected ConcurrentHashMap<String, Robot> robots = new ConcurrentHashMap<>();
    // The map that the world will be using.
    public Maze maze = new EmptyMaze();


    /**
     * getter to get the maze.
     * */
    public Maze getMaze() {
        return maze;
    }

    /**
     * setter to set the maze.
     * */
    public void setMaze(BaseMaze maze) {
        this.maze = maze;
    }

    public Vector<Obstacle> getObstacles() {
        return this.maze.getObstacles();
    }

    public void removeRobot(String name) {
        this.robots.remove(name);
    }

    public void handleCommand(Command command, Server server) {
        command.execute(this, server);
    }

    // Builds a response for every  command input
    public void addRobot(Robot target) {
        this.robots.put(target.getName(), target);
    }

    public Robot getRobot(String name){
        return this.robots.get(name);
    }

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
