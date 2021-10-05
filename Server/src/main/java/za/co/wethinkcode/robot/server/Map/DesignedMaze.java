package za.co.wethinkcode.robot.server.Map;

import za.co.wethinkcode.robot.server.Robot.Position;

@SuppressWarnings("unused")
public class DesignedMaze extends BaseMaze {

    /**
     * Constructor for designed Maze
     * */
    public DesignedMaze(Position position, boolean specified) {
        createMaze();
        addSpecifiedObstacle(specified, position);
    }

    /**
     * Create the maze by calling drawLines with the position to go by.
     * */
    public void createMaze() {
        drawLines(20, -10, 20, 50);
        drawLines(20, 50, -20, 50);
        drawLines(-20, 50, -20, -10);
        drawLines(40, -10, 20, -10);
        drawLines(40, -10, 40, -50);
        drawLines(40 , -50,  -40, -50);
        drawLines(-40, 100, -40, -50);
        drawLines(60, 80, 0, 80);
        drawLines(60, 100, -60, 100);
        drawLines(60, -100, -60, -100);
        drawLines(60, 100, 60, -100);
        drawLines(-60, 100, -60, -60);
        drawLines(-80, 150, -80, -150);
        drawLines(-80, -150, 92, -150);
        drawLines(80, 150, -80, 150);
        drawLines(80, 146, 80, -134);
        drawLines(96, 192, 96, -150);
        drawLines(92, 196, -20, 196);
        drawLines(-50, 196, -100, 196);
        drawLines(-100, -200, -100, -50);
        drawLines(-100, 192, -100, 30);
        drawLines(-100, -200, -30, -200);
        drawLines(10, -200, 92, -200);
        drawLines(10, -184, 92, -184);
        drawLines(96, -188, 96, -196);
    }

    /**
     * drawLines checks which lines are the same, then increments on 5,
     * to draw them.
     * @param newX: new X co-ord;
     * @param newY: new Y co-ord;
     * @param oldX: old X co-ord;
     * @param oldY: old Y co-ord;
     * */
    public void drawLines(int oldX, int oldY, int newX, int newY) {
        if (oldY == newY) {
            for (int i = Math.min(oldX, newX); i < Math.max(oldX, newX) + 5; i += 5) {
                createObstacles(new Position(i, oldY));
            }
        } else if (oldX == newX) {
            for (int i = Math.min(oldY,newY); i < Math.max(oldY,newY)+5; i +=5) {
                createObstacles(new Position(oldX, i));
            }
        }
    }
}