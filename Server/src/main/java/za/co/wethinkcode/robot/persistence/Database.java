package za.co.wethinkcode.robot.persistence;

import net.lemnik.eodsql.QueryTool;
import org.json.simple.parser.ParseException;
import za.co.wethinkcode.robot.ORM.WorldDAI;
import za.co.wethinkcode.robot.ORM.WorldDO;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.World;

import java.sql.SQLException;
import java.sql.Statement;


public class Database {

    private final DatabaseConnection connection;
    public final WorldDAI productQuery;

    public Database(String dbUrl) throws SQLException {
        this.connection = new DatabaseConnection(dbUrl);
        this.connection.connect();
        this.productQuery = QueryTool.getQuery(this.connection.getConnection(), WorldDAI.class );
    }

    public void createDatabase() throws SQLException {
        connection.connect();
        try (Statement stmt = connection.getConnection().createStatement()){
            stmt.executeUpdate("CREATE TABLE worlds (" +
                    "id         INTEGER     NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "name       TEXT        NOT NULL UNIQUE," +
                    "size       INTEGER     NOT NULL," +
                    "data       VARCHAR     NOT NULL)");
            System.out.println("CREATED TABLE SUCCESSFULLY!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("CREATED TABLE SUCCESSFULLY!");
        connection.disconnect();
    }

    public void dropTable() throws SQLException {
        connection.connect();
        try (Statement stmt = connection.getConnection().createStatement()){
            stmt.executeUpdate("DROP TABLE worlds;");
            System.out.println("DELETED TABLE SUCCESSFULLY!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        connection.disconnect();
    }

    public void saveWorld(World world, String name) throws SQLException {
        connection.connect();
        world.getMaze().addAllObstacles(world);

        this.productQuery.saveWorld(world.maze.getObjects().toString(), name, world.BOTTOM_RIGHT.getX() * 2);

        System.out.println("WORLD SAVED SUCCESSFULLY!");
        connection.disconnect();
    }

    public boolean readWorld(World world, String name) throws SQLException, ParseException {
        this.connection.connect();
        WorldDO dataObject =  this.productQuery.readWorld(name);
        int worldSize = dataObject.getWorldSize();

        Position BOTTOM_RIGHT = new Position((worldSize/2),(-worldSize/2));
        Position TOP_LEFT = new Position((-worldSize/2),(worldSize/2));

        world.setTOP_LEFT(TOP_LEFT);
        world.setBOTTOM_RIGHT(BOTTOM_RIGHT);
        world.maze.restoreAllObstacles(dataObject.getWorldData());

        System.out.println("WORLD \"" + name + "\" HAS BEEN LOADED.");
        connection.disconnect();

        return true;
    }

    public String getWorldObjects(String name) throws SQLException {
        this.connection.connect();
        WorldDO dataObject =  this.productQuery.readWorld(name);
        
        return dataObject.getWorldData();
    }
}
