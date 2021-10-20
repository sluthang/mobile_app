package za.co.wethinkcode.robot.persistence;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robot.ORM.WorldDO;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.World;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseTest {

    @Test
    void readingAndSavingAWorld() throws SQLException, ParseException {
        Database database = new Database("jdbc:sqlite:test_db.sqlite");
        World world = setUpEmptyWorld();
        String expectedWorldData = "{\"objects\":[{\"position\":[1,1],\"type\":\"OBSTACLE\"}]}";

        database.createDatabase();
        database.saveWorld(world, "saveTest");
        WorldDO data = database.productQuery.readWorld("saveTest");

        assertEquals("saveTest",data.getWorldName());
        assertEquals(4, data.getWorldSize());
        assertEquals(expectedWorldData, data.getWorldData());

        database.dropTable();
    }

    @Test
    void addObstacleListType() {
    }

    @Test
    void addAllObstacles() {
    }

    @Test
    void readWorld() throws SQLException, ParseException {
        Database database = new Database("jdbc:sqlite:uss_victory_db.sqlite");
        WorldDO data = database.productQuery.readWorld("beau");
        String expectedWorldData = "{\"objects\":[{\"position\":[1,3],\"type\":" +
                "\"OBSTACLE\"},{\"position\":[1,0],\"type\":\"PIT\"}]}";

        assertEquals("beau",data.getWorldName());
        assertEquals(2, data.getWorldSize());
        assertEquals(expectedWorldData, data.getWorldData());
    }

    World setUpEmptyWorld(){
        int worldSize = 4;
        Position BOTTOM_RIGHT = new Position((worldSize/2),(-worldSize/2));
        Position TOP_LEFT = new Position((-worldSize/2),(worldSize/2));
        World world = new World("emptymaze", BOTTOM_RIGHT, TOP_LEFT, new Position(1,1), true);

        return world;
    }
}