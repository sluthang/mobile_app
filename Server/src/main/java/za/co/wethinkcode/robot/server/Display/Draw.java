package za.co.wethinkcode.robot.server.Display;

import za.co.wethinkcode.robot.server.Map.Obstacle;
import za.co.wethinkcode.robot.server.Robot.Robot;

import java.awt.*;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class Draw {
    private final Turtle display;

    public Draw() {
        Turtle.setCanvasSize(400, 600);
        Turtle.zoomFit();
        this.display = new Turtle();
        display.hide();
        display.fillColor(Color.BLACK);
        display.outlineWidth(2);
        display.width(2);
        display.speed(0);
        display.shape("triangle");
        display.shapeSize(7, 7);
    }

    public void drawObstacles(Vector<Obstacle> list, Color color) {
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

    public void clear() {
        display.clear();
    }
}
