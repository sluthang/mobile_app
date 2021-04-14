package za.co.wethinkcode.robot.server.Map;

import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Robot.Robot;
import za.co.wethinkcode.robot.server.Robot.UpdateResponse;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Interface to represent a maze. A World will be loaded with a Maze, and will delegate the work to check if a path is blocked by certain obstacles etc to this maze instance.
 */
public interface Maze {
    /**
     * @return the list of obstacles, or an empty list if no obstacles exist.
     */
    Collection<SquareObstacle> getObstacles();

    Collection<Pits> getPits();

    Collection<Mines> getMines();

    Position hitMine(Position a, Position b);

    /**
     * Checks if this maze has at least one obstacle that blocks the path that goes from coordinate (x1, y1) to (x2, y2).
     * Since our robot can only move in horizontal or vertical lines (no diagonals yet), we can assume that either x1==x2 or y1==y2.
     * @param a first position
     * @param b second position
     * @return `true` if there is an obstacle is in the way
     */
    UpdateResponse blocksPath(Position a, Position b, ConcurrentHashMap<String, Robot> robots);
}
