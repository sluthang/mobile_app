
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robot.server.Robot.Position;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PositionTests {

    @Test
    public void testEquals() {
        Position pos = new Position(0,0);
        assertEquals(pos, new Position(0, 0));
    }
}
