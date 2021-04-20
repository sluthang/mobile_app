package za.co.wethinkcode.robot.server.Display;

import za.co.wethinkcode.robot.server.Map.Obstacle;
import za.co.wethinkcode.robot.server.MultiServer;
import za.co.wethinkcode.robot.server.Robot.Robot;

import java.awt.*;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class Draw {
    private final Turtle display;
    private final int height;
    private final int width;

    public Draw(int height, int width) {
        this.width = width;
        this.height = height;
        Turtle.setCanvasSize(600, 600);
        this.display = new Turtle();
        display.hide();
        display.fillColor(Color.BLACK);
        display.outlineWidth(2);
        display.width(2);
        display.speed(0);
        display.shape("triangle");
        display.shapeSize(7, 7);

    }

    public void zoom() {
        Turtle.zoomFit();
    }

    private void drawBorder() {
        display.penColor(Color.BLACK);
        display.up();
        display.setPosition(-(width/2), (height/2));
        display.setDirection(0);
        display.down();
        display.forward(width);
        display.right(90);
        display.forward(height);
        display.right(90);
        display.forward(width);
        display.right(90);
        display.forward(height);
        display.up();
    }

    /**
     * Draws all the current obstacles on the field in the turtle draw interface.
     * @param list to be drawn.
     * @param color of the obstacles.
     */
    public void drawObstacles(Vector<Obstacle> list, Color color) {
        drawBorder();
        display.penColor(color);
        for (Obstacle obs : list) {
            display.up();
            display.setPosition(obs.getBottomLeftX()+(obs.getSize()/2),
                    obs.getBottomLeftY()+(obs.getSize()/2));
            display.dot(color, obs.getSize()+3);

        }
        display.up();
        display.setPosition(0, 0);
        display.setDirection(0);
        display.forward(2);
    }

    /**
     * Draws the robots on the field in the turtle draw interface.
     * @param robots list of robots to draw.
     * @param color of the robots.
     */
    public void drawRobots(ConcurrentHashMap<String, Robot> robots, Color color) {
        Set<String> keys = robots.keySet();
        for (String key : keys) {
            Robot robot = robots.get(key);
            display.up();
            display.setPosition(robot.getPosition().getX(), robot.getPosition().getY());
            display.dot(color, 3);
        }
        display.up();
        display.setPosition(0,-20);
    }

    /**
     * Clears the display of all drawn objects.
     */
    public void clear() {
        display.clear();
    }
}
