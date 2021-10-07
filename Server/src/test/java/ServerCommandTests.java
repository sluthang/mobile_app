import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robot.persistence.Database;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.World;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ServerCommandTests {

    @Test
    public void restoreCommand() throws SQLException {
        int worldSize = 4;
        Position BOTTOM_RIGHT = new Position((worldSize/2),(-worldSize/2));
        Position TOP_LEFT = new Position((-worldSize/2),(worldSize/2));

        World world = new World("emptymaze", BOTTOM_RIGHT, TOP_LEFT, new Position(1,1), false);

        assertEquals(2, world.BOTTOM_RIGHT.getX());
        assertEquals(0,world.getObstacles().size());

        Database database = new Database();
        database.readWorld(world, "beau");

        assertEquals(1, world.BOTTOM_RIGHT.getX());
        assertEquals(1, world.getObstacles().size());
    }
}
