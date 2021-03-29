package za.co.wethinkcode.robot.server.Map;

import za.co.wethinkcode.robot.server.Robot.Position;

import java.util.ArrayList;
import java.util.List;

public class BaseMaze implements Maze {

    protected List<Obstacle> obstaclesList = new ArrayList<>();

    /**
     * setter to set the obstacles.
     * @param obstacle: takes param of a list obstacle.
     * */
    public void setObstacles(List<Obstacle> obstacle) {
        this.obstaclesList = obstacle;
    }

    /**
     * getter to get the obstacles.
     * */
    public List<Obstacle> getObstacles() {
        return this.obstaclesList;
    }

    /**
     * Create the obstacles list, by adding in a new obstacles at the position.
     * @param position: takes in the position, and pass it into the new square position.
     * */
    public void createObstacles(Position position) {
        this.obstaclesList.add(new SquareObstacle(position.getX(), position.getY()));
    }

    /**
     * Takes in 2 parameters, old and new position and checks if the path is blocked per each obstacle in the
     * obstacle list.
     * @param a: the old position;
     * @param b: the new position;
     * @return: returns true if the path is blocked.
     * */
    public boolean blocksPath(Position a, Position b) {
        for (Obstacle obst : this.obstaclesList) {
            if (obst.blocksPath(a,b))
                return true;
        }
        return false;
    }
}

