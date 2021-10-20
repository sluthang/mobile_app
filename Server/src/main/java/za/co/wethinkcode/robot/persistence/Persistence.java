package za.co.wethinkcode.robot.persistence;

import za.co.wethinkcode.robot.server.Map.Obstacle;
import za.co.wethinkcode.robot.server.World;

import java.sql.SQLException;
import java.util.Vector;

public interface Persistence {


    void createDatabase() throws SQLException;

    void dropTable() throws SQLException;

    void saveWorld(World world, String name, int size) throws SQLException;

    void updateWorld(String name);

    void deleteWorld(String name);

    boolean readWorld(World world, String name) throws SQLException;

    void addObstacleListType(Vector <Obstacle> objects, String type);

    void addAllObstacles(World world);

}
