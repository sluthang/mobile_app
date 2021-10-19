package za.co.wethinkcode.robot.persistence;
import net.lemnik.eodsql.QueryTool;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import za.co.wethinkcode.robot.ORM.WorldDAI;
import za.co.wethinkcode.robot.ORM.WorldDO;
import za.co.wethinkcode.robot.server.Map.Obstacle;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.World;

import java.sql.*;
import java.util.Vector;


public class Database implements Persistence    {

    public JSONArray obstacles = new JSONArray();
    public JSONObject objects = new JSONObject();
    private final DatabaseConnection connection;
    public final WorldDAI productQuery;

    public Database(String dbUrl) throws SQLException {
        this.connection = new DatabaseConnection(dbUrl);
        this.connection.connect();
        this.productQuery = QueryTool.getQuery(this.connection.getConnection(), WorldDAI.class );
    }

    @Override
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
        connection.disconnect();
    }

    @Override
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
        addAllObstacles(world);
        connection.connect();
        
        this.productQuery.saveWorld(this.objects.toString(), name, world.BOTTOM_RIGHT.getX() * 2);

        System.out.println("World save successfully!");
        connection.disconnect();
    }

    @Override
    public void updateWorld(String name) {
    }

    @Override
    public void deleteWorld(String name) {
    }

    public boolean readWorld(World world, String name) throws SQLException, ParseException {
        this.connection.connect();
        WorldDO dataObject =  this.productQuery.readWorld(name);
        int worldSize = dataObject.getWorldSize();

        Position BOTTOM_RIGHT = new Position((worldSize/2),(-worldSize/2));
        Position TOP_LEFT = new Position((-worldSize/2),(worldSize/2));

        world.setTOP_LEFT(TOP_LEFT);
        world.setBOTTOM_RIGHT(BOTTOM_RIGHT);
        System.out.println(dataObject.getWorldData());
        world.maze.restoreAllObstacles(dataObject.getWorldData());

        System.out.println("World " + name + " has been loaded.");
        connection.disconnect();

        return true;
    }

    @Override
    public void addObstacleListType(Vector<Obstacle> objects, String type) {
        for (Obstacle obstacle: objects){
            this.obstacles.put(new JSONObject().put("type", type).put("position",
                    new JSONArray().put(obstacle.getBottomLeftX()).put(obstacle.getBottomLeftY())));
        }
    }

    @Override
    public void addAllObstacles(World world) {
        addObstacleListType(world.getMaze().getObstacles(), "OBSTACLE");
        addObstacleListType(world.getMaze().getPits(), "PIT");
        addObstacleListType(world.getMaze().getMines(), "MINE");

        for (int i = 0; i < obstacles.length(); i++){
            this.objects.append("objects", obstacles.get(i));
        }
    }
}
