package za.co.wethinkcode.robot.persistence;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import za.co.wethinkcode.robot.server.Map.Obstacle;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.World;

import java.sql.*;
import java.util.Vector;


public class Database implements Persistence    {

    public JSONArray obstacles = new JSONArray();
    public JSONObject objects = new JSONObject();
    private final DatabaseConnection connection;

    public Database(String dbUrl){
        this.connection = new DatabaseConnection(dbUrl);
    }

    @Override
    public void createDatabase(World world) throws SQLException {
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
    public void saveWorld(World world, String name, int size) throws SQLException {
        connection.connect();
        addAllObstacles(world);
        String SQL = "INSERT INTO worlds (size, name, data) VALUES (?, ?, ?)";
        try(PreparedStatement statement = connection.getConnection().prepareStatement(SQL)){
            statement.setInt(1, size);
            statement.setString(2, name);
            statement.setString(3, this.objects.toString());
            final boolean resultSet = statement.execute();

            if(resultSet){
                throw new RuntimeException("Got unexpected SQL result set.");
            } else {
                System.out.println("World save successfully!");
            }
        } catch (SQLException e) {
            if(e.getErrorCode() == 19){
                System.out.println("World name already exists.");
            }
        }
        connection.disconnect();
    }

    @Override
    public void updateWorld(String name) {
    }

    @Override
    public void deleteWorld(String name) {
    }

    @Override
    public void readWorld(World world, String name) throws SQLException {
        connection.connect();
        String SQL = "SELECT size, data FROM worlds WHERE name = ?";

        try(PreparedStatement statement = connection.getConnection().prepareStatement(SQL)){
            statement.setString(1, name);
            final boolean resultSet = statement.execute();

            if(!resultSet) {
                throw new RuntimeException("Got unexpected SQL result set.");
            }
            try(ResultSet results = statement.getResultSet()){
                int worldSize = results.getInt("size");
                Position BOTTOM_RIGHT = new Position((worldSize/2),(-worldSize/2));
                Position TOP_LEFT = new Position((-worldSize/2),(worldSize/2));

                world.setTOP_LEFT(TOP_LEFT);
                world.setBOTTOM_RIGHT(BOTTOM_RIGHT);
                world.maze.restoreAllObstacles(results.getString("data"));

                System.out.println("World " + name + " has been loaded");
            }
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }
        connection.disconnect();
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
