package za.co.wethinkcode.robot.server.World;


import za.co.wethinkcode.robot.server.Map.Maze;
import za.co.wethinkcode.robot.server.Map.Obstacle;
import za.co.wethinkcode.robot.server.ResponseBuilder;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Robot.Robot;

import java.util.Hashtable;
import java.util.List;

public abstract class AbstractWorld implements IWorld {
    protected static final Position CENTRE = new Position(0,0);
    protected  Hashtable<String, Robot> robots = new Hashtable<>();
    public ResponseBuilder response;

    public Maze maze;

    /**
     * getter to get the maze.
     * */
    public Maze getMaze() {
        return maze;
    }

    /**
     * setter to set the maze.
     * */
    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    /**
     * getter to return the list of obstacles.
     * */
    public List<Obstacle> getObstacles() {
        return this.maze.getObstacles();
    }

    public void removeRobot(String name) {
        this.robots.remove(name);
    }
}