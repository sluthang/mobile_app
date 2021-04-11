package za.co.wethinkcode.robot.server.World;

import za.co.wethinkcode.robot.server.ConfigReader;
import za.co.wethinkcode.robot.server.Map.EmptyMaze;
import za.co.wethinkcode.robot.server.Map.Maze;
import za.co.wethinkcode.robot.server.Map.Mines;
import za.co.wethinkcode.robot.server.Map.Obstacle;
import za.co.wethinkcode.robot.server.Map.Pits;
import za.co.wethinkcode.robot.server.ResponseBuilder;
import za.co.wethinkcode.robot.server.Robot.Robot;

import java.util.Dictionary;
import java.util.Hashtable;

public class World extends AbstractWorld {
    // The map that the world will be using.
    public static Maze maze = new EmptyMaze();
    // Builds a response for every  command input

    /**
     * Constructor for the World Class.
     * */
    public World() {
    }

    public void addRobot(Robot target) {
        this.robots.put(target.getName(), target);
    }

    public Robot getRobot(String name){
        return this.robots.get(name);
    }

    public Hashtable<String, Robot> getRobots() {
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
        for (Pits pit : maze.getPits()) {
            System.out.println("Pit- At position "+ pit.getBottomLeftX()+ "," +pit.getBottomLeftY()+ " (to "+
                    (pit.getBottomLeftX() + 4) + "," + (pit.getBottomLeftY() + 4) + ")");
        }
        for (Mines mine : maze.getMines()) {
            System.out.println("Mine- At position "+ mine.getBottomLeftX()+ "," +mine.getBottomLeftY()+ " (to "+
                    (mine.getBottomLeftX()) + "," + (mine.getBottomLeftY()) + ")");
        }
    }
}
