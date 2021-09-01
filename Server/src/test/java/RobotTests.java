import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robot.server.Robot.Direction;
import za.co.wethinkcode.robot.server.Robot.Robot;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RobotTests {

    @Test
    public void testUpdateDirectionRight() {
        Robot robot = new Robot("hal");
        assertEquals(robot.getCurrentDirection(), Direction.NORTH);

        robot.updateDirection(true);
        assertEquals(robot.getCurrentDirection(), Direction.EAST);
    }

    @Test
    public void testUpdateDirectionLeft() {
        Robot robot = new Robot("hal");
        assertEquals(robot.getCurrentDirection(), Direction.NORTH);

        robot.updateDirection(false);
        assertEquals(robot.getCurrentDirection(), Direction.WEST);
    }

    @Test
    public void testIsDead() {
        Robot robot = new Robot("hal");
        robot.setShields(-1);
        assertEquals(robot.isDead(), "DEAD");
    }

}
