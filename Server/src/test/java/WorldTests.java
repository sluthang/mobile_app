import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robot.server.Map.SquareObstacle;
import za.co.wethinkcode.robot.server.Robot.Position;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorldTests {

    @Test
    public void testObstaclesBlocksPath() {
        SquareObstacle obs = new SquareObstacle(0,5);
        assertTrue(obs.blocksPath(new Position(0,0), new Position(0,10)));
        assertFalse(obs.blocksPath(new Position(0,0), new Position(0,4)));
    }
}
