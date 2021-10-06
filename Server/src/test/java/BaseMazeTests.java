import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robot.server.Map.BaseMaze;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseMazeTests {

    @Test
    public void restoreAllObstaclesTest() throws ParseException {
        BaseMaze maze = new BaseMaze();
        String data = "{\"objects\":[{\"position\":[1,3],\"type\":\"OBSTACLE\"}]}";

        assertEquals(maze.getObstacles().size(), 0);

        maze.restoreAllObstacles(data);
        assertEquals(maze.getObstacles().size(), 1);
    }
}
