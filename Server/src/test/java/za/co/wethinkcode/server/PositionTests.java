package za.co.wethinkcode.server;
import static org.junit.Assert.*;
import org.junit.Test;
import za.co.wethinkcode.robot.server.MultiServer;
import za.co.wethinkcode.robot.server.Robot.Position;

import java.io.IOException;

public class PositionTests {

    @Test
    public void testEquals() {
        Position pos = new Position(0,0);
        assertEquals(pos, new Position(0, 20));
    }
}
