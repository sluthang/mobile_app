package za.co.wethinkcode.robot.server.Map;

import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Robot.UpdateResponse;

import java.util.*;

public class BaseMaze implements Maze {

    Vector<Obstacle> obstaclesList = new Vector<>();
    Vector<Obstacle> pitsList = new Vector<>();
    Vector<Obstacle> minesList = new Vector<>();


    /**
     * setter to set the obstacles.
     * @param obstacle: takes param of a list obstacle.
     * */
    public void setObstacles(Vector<Obstacle> obstacle) {
        this.obstaclesList = obstacle;
    }

    /**
     * getter to get the obstacles.
     * */
    public Vector<Obstacle> getObstacles() {
        return this.obstaclesList;
    }

    public Vector<Obstacle> getPits() {
        return pitsList;
    }

    public Vector<Obstacle> getMines() {
        return minesList;
    }

    /**
     * Create the obstacles list, by adding in a new obstacles at the position.
     * @param position: takes in the position, and pass it into the new square position.
     * */
    public void createObstacles(Position position) {
        this.obstaclesList.add(new SquareObstacle(position.getX(), position.getY()));
    }

    public void createPit(Position position) {
        this.pitsList.add(new Pits(position.getX(), position.getY()));
    }

    public void createMine(Position position) {
        this.minesList.add(new Mines(position.getX(), position.getY()));
    }



    /**
     * Takes in 2 parameters, old and new position and checks if the path is blocked per each obstacle in the
     * obstacle list.
     * @param a : the old position;
     * @param b : the new position;
     * @return: returns true if the path is blocked.
     * */
    public UpdateResponse blocksPath(Position a, Position b) {
        for (Obstacle pit : this.pitsList) {
            if (pit.blocksPath(a,b))
                return UpdateResponse.FAILED_BOTTOMLESS_PIT;
        }

        for (Obstacle obst : this.obstaclesList) {
            if (obst.blocksPath(a,b)) {
                return UpdateResponse.FAILED_OBSTRUCTED;
            }
        }

        for (Obstacle mine : this.minesList) {
            if (mine.blocksPath(a,b)) {
                return UpdateResponse.FAILED_HIT_MINE;
            }
        }
        return UpdateResponse.SUCCESS;
    }

    public Position hitMine(Position a, Position b) {
        Iterator<Obstacle> i = this.minesList.iterator();

        while (i.hasNext()) {
            Obstacle mine = i.next();
            if (mine.blocksPath(a, b)) {
                Position newPos = new Position(mine.getBottomLeftX(), mine.getBottomLeftY());
                i.remove();
                return newPos;
            }
        }
        return b;
    }
}

