package server.Map;

import za.co.wethinkcode.robot.server.Robot.Position;

public class SquareObstacle implements Obstacle {

    private final int bottomLeftX;
    private final int bottomLeftY;

    /**
     * Constructor for the SquareObstacles class, sets the positions
     * @param x: position x;
     * @param y: position y;
     * */
    public SquareObstacle(int x, int y) {
        this.bottomLeftX = x;
        this.bottomLeftY = y;
    }

    /**
     * Getter for the bottom left X co-ordinate
     * */
    public int getBottomLeftX() {
        return this.bottomLeftX;
    }

    /**
     * Getter for the bottom left Y co-ordinate
     * */
    public int getBottomLeftY() {
        return this.bottomLeftY;
    }

    /**
     * Getter for the size of the obstacle (square 5x5 on curriculum page).
     * */
    public int getSize() {
        return 5;
    }

    /**
     * Boolean to check if the current position that is passed is blocked off or not. (i.e inside of an obstacle).
     * @param position: Pass through the current position to check;
     * @return: true if blocked (inside an obs.)
     * */
    public boolean blocksPosition(Position position) {
        return (position.getX() >= bottomLeftX && position.getX() <= (bottomLeftX + 4)) && (position.getY() >= bottomLeftY && position.getY() <= (bottomLeftY + 4));
    }

    /**
     * Checks if the path you are moving towards is blocked. First checks if we are moving along x or y axis, Then if the
     * number is between the smaller(start) and bigger(end) position moving along the axis.
     * @param a: the old position original position;
     * @param b: the new position to move to;
     * @return: true if the path is blocked.
     * */
    public boolean blocksPath(Position a, Position b) {
        if (a.getX() == b.getX()) {
            for (int i = Math.min(a.getY(), b.getY()); i <= Math.max(a.getY(), b.getY()); i++) {
                if (blocksPosition(new Position(a.getX(), i))){
                    return true;
                }
            }
        } else if (a.getY() == b.getY()) {
            for (int i = Math.min(a.getX(), b.getX()); i <= Math.max(a.getX(), b.getX()); i++) {
                if (blocksPosition(new Position(i, a.getY()))){
                    return true;
                }
            }
        }
        return false;
    }
}
