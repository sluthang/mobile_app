package za.co.wethinkcode.robot.server.World;

import za.co.wethinkcode.robot.server.ConfigReader;
import za.co.wethinkcode.robot.server.Map.EmptyMaze;
import za.co.wethinkcode.robot.server.Map.Maze;
import za.co.wethinkcode.robot.server.Map.Obstacle;
import za.co.wethinkcode.robot.server.Robot.Robot;

public class World extends AbstractWorld {
    // The map that the world will be using.
    public static Maze maze = new EmptyMaze();

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

    /**
     * Shows the obstacles that are inside of the obstacle list.
     * printing them in the terminal at the positions of the obstacles.
     * */
    public void showObstacles() {
        maze.getObstacles();
        for (Obstacle maze : maze.getObstacles()) {
            System.out.println("- At position "+ maze.getBottomLeftX()+ "," +maze.getBottomLeftY()+ " (to "+
                    (maze.getBottomLeftX() + 4) + "," + (maze.getBottomLeftY() + 4) + ")");
        }
    }
}
