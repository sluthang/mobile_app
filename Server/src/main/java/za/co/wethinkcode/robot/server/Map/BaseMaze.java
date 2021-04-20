package za.co.wethinkcode.robot.server.Map;

import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Robot.Robot;
import za.co.wethinkcode.robot.server.Robot.UpdateResponse;
import za.co.wethinkcode.robot.server.Server.Server;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    public UpdateResponse blocksPath(Position a, Position b, ConcurrentHashMap<String, Robot> robots, String robotName) {

        int incX = 1;
        int incY = 1;
        if (a.getX() > b.getX()) incX = -1;
        if (a.getY() > b.getY()) incY = -1;

        int x = a.getX();
        do {
            int y = a.getY();
            do {
                for (Obstacle pit : this.pitsList) {
                    if (pit.blocksPosition(new Position(x, y)))
                        return UpdateResponse.FAILED_BOTTOMLESS_PIT;
                }

                for (Obstacle obst : this.obstaclesList) {
                    if (obst.blocksPosition(new Position(x, y))) {
                        return UpdateResponse.FAILED_OBSTRUCTED;
                    }
                }

                for (Obstacle mine : this.minesList) {
                    if (mine.blocksPosition(new Position(x, y))) {
                        return UpdateResponse.FAILED_HIT_MINE;
                    }
                }

                Set<String> keys = robots.keySet();
                for (String key : keys) {
                    if (key.equals(robotName)) {
                        continue;
                    }
                    //TODO robots do not colide correctly and get stuck inside one another.
                    if (robots.get(key).blocksPosition(new Position(x,y))) {
                        return UpdateResponse.FAILED_OBSTRUCTED;
                    }
                }
                if (y != b.getY()) {
                    y += incY;
                }
            } while (y != b.getY());
            if (x != b.getX()) {
                x += incX;
            }
        } while  (x != b.getX());
        return UpdateResponse.SUCCESS;
    }

    public void hitMine(Position robotPosition, Server server) {
        Iterator<Obstacle> i = this.minesList.iterator();

        while (i.hasNext()) {
            Obstacle mine = i.next();
            if (mine.blocksPosition(robotPosition)) {
                server.robot.takeDamage(3);
                i.remove();
            }
        }
    }

    public UpdateResponse blocksPosition(ConcurrentHashMap<String, Robot> robots, Position position, String robotName) {
        for (Obstacle pit : this.pitsList) {
            if (pit.blocksPosition(position))
                return UpdateResponse.FAILED_BOTTOMLESS_PIT;
        }

        for (Obstacle obst : this.obstaclesList) {
            if (obst.blocksPosition(position)) {
                return UpdateResponse.FAILED_OBSTRUCTED;
            }
        }

        for (Obstacle mine : this.minesList) {
            if (mine.blocksPosition(position)) {
                return UpdateResponse.FAILED_HIT_MINE;
            }
        }

        Set<String> keys = robots.keySet();
        for (String key : keys) {
            if (key.equals(robotName)) {
                continue;
            }
            if (robots.get(key).blocksPosition(position)) {
                return UpdateResponse.FAILED_OBSTRUCTED;
            }
        }
        return UpdateResponse.SUCCESS;
    }
}