package za.co.wethinkcode.robot.server.World;


import za.co.wethinkcode.robot.server.Map.Maze;
import za.co.wethinkcode.robot.server.ResponseBuilder;
import za.co.wethinkcode.robot.server.Map.SquareObstacle;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Robot.Robot;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractWorld implements IWorld {
    protected static final Position CENTRE = new Position(0,0);
    public ResponseBuilder response;
    protected ConcurrentHashMap<String, Robot> robots = new ConcurrentHashMap<>();

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


    public Collection<SquareObstacle> getObstacles() {
        return this.maze.getObstacles();
    }

    public void removeRobot(String name) {
        this.robots.remove(name);
    }
}