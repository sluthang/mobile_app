package za.co.wethinkcode.robot.server.World;

import za.co.wethinkcode.robot.server.Map.Maze;
import za.co.wethinkcode.robot.server.Map.Obstacle;
import za.co.wethinkcode.robot.server.Robot.Robot;
import za.co.wethinkcode.robot.server.World.AbstractWorld;

public class World extends AbstractWorld {

    /**
     * Constructor for the World Class.
     * @param maze: the maze that it takes in;
     * */
    public World(Maze maze) {
        this.maze = maze;
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
