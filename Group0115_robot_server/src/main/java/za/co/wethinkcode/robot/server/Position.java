package za.co.wethinkcode.robot.server;

public class Position {
    public int x;
    public int y;
    public String direction;

    Position() {
        this.x = 0;
        this.y = 0;
        this.direction = "NORTH";
    }

    public boolean updatePosition(int nrSteps) {
        switch (this.direction) {
            case "NORTH":
                this.y += nrSteps;
                break;
            case "WEST":
                this.x += -nrSteps;
                break;
            case "EAST":
                this.x += nrSteps;
                break;
            case "SOUTH":
                this.y += -nrSteps;
                break;
        }
        return true;
    }

    public String getDirection() {
        return direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
