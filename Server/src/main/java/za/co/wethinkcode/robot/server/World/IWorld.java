package server.World;

import za.co.wethinkcode.robot.server.Map.Maze;
import za.co.wethinkcode.robot.server.Map.Obstacle;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Robot.Robot;

import java.util.List;

/**
 * Your Text and Turtle worlds must implement this interface.
 */
public interface IWorld {

    Position CENTRE = new Position(0,0);

    /**
     * @return the list of obstacles, or an empty list if no obstacles exist.
     */
    List<Obstacle> getObstacles();

    /**
     * Gives opportunity to world to draw or list obstacles.
     */
    void showObstacles();

    /**
     * getter to get the maze.
     * */
    Maze getMaze();

    void addRobot(Robot target);

    Robot getRobot(String name);

}