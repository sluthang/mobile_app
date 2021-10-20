package za.co.wethinkcode.robot.persistence;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.World;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseTest {

    @Test
    void saveWorld() throws SQLException {
        int worldSize = 4;
        Position BOTTOM_RIGHT = new Position((worldSize/2),(-worldSize/2));
        Position TOP_LEFT = new Position((-worldSize/2),(worldSize/2));

        Database database = new Database("jdbc:sqlite:test_db.sqlite");
        database.createDatabase();

        World world = new World("emptymaze", BOTTOM_RIGHT, TOP_LEFT, new Position(1,1), true);

        database.saveWorld(world, "saveTest", 10);
        database.readWorld(world, "saveTest");

        assertEquals(5, world.BOTTOM_RIGHT.getX());
        assertEquals(-5, world.TOP_LEFT.getX());
        database.dropTable();

    }

    @Test
    void addObstacleListType() {
    }

    @Test
    void addAllObstacles() {
    }
}