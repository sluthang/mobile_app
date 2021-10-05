package za.co.wethinkcode.robot.persistence;
import org.json.JSONArray;
import org.json.JSONObject;
import za.co.wethinkcode.robot.server.Map.Obstacle;
import za.co.wethinkcode.robot.server.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;


public class Database implements Persistence    {

    public JSONArray obstacles = new JSONArray();
    public JSONObject objects = new JSONObject();
    private DatabaseConnection connection;
    private Connection conn;

    public Database() throws SQLException {
        this.connection = new DatabaseConnection("jdbc:sqlite:uss_victory_db.sqlite");
        conn = connection.connect();
    }

    @Override
    public void createDatabase(World world) {
    }

    @Override
    public void saveWorld(World world, String name, int size) {
        addAllObstacles(world);
        String SQL = "INSERT INTO worlds (size, name, data) VALUES (?, ?, ?)";
        try(PreparedStatement statement = conn.prepareStatement(SQL)){
            statement.setInt(1, 1);
            statement.setString(2, name);
            statement.setString(3, this.objects.toString());
            final boolean resultSet = statement.execute();

            if(resultSet){
                throw new RuntimeException("Got unexpected SQL result set.");
            } else {
                System.out.println("World save successfully!");
            }
//            statement.executeUpdate("INSERT INTO worlds (size, name, data) " +
//                    "VALUES ("+size+",'"+name+"', '"+this.objects+"');");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void updateWorld() {
    }

    @Override
    public void deleteWorld() {
    }

    @Override
    public void readWorld() {
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

    @Override
    public void checkWorldNameExistence() {
        try(Statement statement = conn.createStatement()){
            statement.executeUpdate("");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
