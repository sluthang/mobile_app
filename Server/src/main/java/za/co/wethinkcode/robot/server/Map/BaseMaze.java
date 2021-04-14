package za.co.wethinkcode.robot.server.Map;

import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Robot.Robot;
import za.co.wethinkcode.robot.server.Robot.UpdateResponse;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BaseMaze implements Maze {

    Collection<SquareObstacle> obstaclesList = Collections.synchronizedCollection(new ArrayList<>());
    Collection<Pits> pitsList = Collections.synchronizedCollection(new ArrayList<>());
    Collection<Mines> minesList = Collections.synchronizedCollection(new ArrayList<>());


    /**
     * setter to set the obstacles.
     * @param obstacle: takes param of a list obstacle.
     * */
    public void setObstacles(Collection<SquareObstacle> obstacle) {
        this.obstaclesList = obstacle;
    }

    /**
     * getter to get the obstacles.
     * */
    public Collection<SquareObstacle> getObstacles() {
        return this.obstaclesList;
    }

    public Collection<Pits> getPits() {
        return pitsList;
    }

    public Collection<Mines> getMines() { return minesList; }

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
    public UpdateResponse blocksPath(Position a, Position b, ConcurrentHashMap<String, Robot> robots) {
        int incX = 1;
        int incY = 1;
        if (a.getX() > b.getX()) incX = -1;
        if (a.getY() > b.getY()) incY = -1;

        for (int x = a.getX() + incX; x != b.getX(); x += incX) {
            for (int y = a.getY() + incY; y != b.getY(); y += incY) {
                for (Pits pit : this.pitsList) {
                    if (pit.blocksPosition(new Position(x, y)))
                        return UpdateResponse.FAILED_BOTTOMLESS_PIT;
                }

                for (Obstacle obst : this.obstaclesList) {
                    if (obst.blocksPosition(new Position(x, y))) {
                        return UpdateResponse.FAILED_OBSTRUCTED;
                    }
                }

                for (Mines mine : this.minesList) {
                    if (mine.blocksPosition(new Position(x, y))) {
                        return UpdateResponse.FAILED_HIT_MINE;
                    }
                }

                Set<String> keys = robots.keySet();
                for (String key : keys) {
                    if (robots.get(key).blocksPosition(new Position(x, y))) {
                        return UpdateResponse.FAILED_OBSTRUCTED;
                    }
                }
            }
        }
        return UpdateResponse.SUCCESS;
    }

    public Position hitMine(Position a, Position b) {
        Iterator<Mines> i = this.minesList.iterator();

        while (i.hasNext()) {
            Mines mine = i.next();
            if (mine.blocksPath(a, b)) {
                Position newPos = new Position(mine.getBottomLeftX(), mine.getBottomLeftY());
                i.remove();
                return newPos;
            }
        }
        return b;
    }
}