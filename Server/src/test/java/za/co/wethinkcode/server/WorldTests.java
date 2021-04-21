package za.co.wethinkcode.server;

import static org.junit.Assert.*;
import org.junit.Test;
import za.co.wethinkcode.robot.server.Map.SquareObstacle;
import za.co.wethinkcode.robot.server.Robot.Position;

public class WorldTests {

    @Test
    public void testObstaclesBlocksPath() {
        SquareObstacle obs = new SquareObstacle(0,5);
        assertTrue(obs.blocksPath(new Position(0,0), new Position(0,10)));
        assertFalse(obs.blocksPath(new Position(0,0), new Position(0,4)));
    }
}
