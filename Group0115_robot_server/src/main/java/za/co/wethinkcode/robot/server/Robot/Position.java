package za.co.wethinkcode.robot.server.Robot;

public class Position {
    private final int x;
    private final int y;

    /**
     * constructor for the positions
     * */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for the x co-ordinate
     * */
    public int getX() {
        return x;
    }

    /**
     * Getter for the Y co-ordinate
     * */
    public int getY() {
        return y;
    }

    /**
     * This is an override to override the default equals object with our own version of an equals().
     * it is used to check the position and if it is equals.
     * */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (x != position.x) return false;
        return y == position.y;
    }

    /**
     * boolean to check if the position is inside of the rectangle border.
     * */
    public boolean isIn(Position topLeft, Position bottomRight) {
        boolean withinTop = this.y <= topLeft.getY();
        boolean withinBottom = this.y >= bottomRight.getY();
        boolean withinLeft = this.x >= topLeft.getX();
        boolean withinRight = this.x <= bottomRight.getX();
        return withinTop && withinBottom && withinLeft && withinRight;
    }
}
