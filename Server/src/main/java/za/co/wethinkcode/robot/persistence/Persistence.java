package za.co.wethinkcode.robot.persistence;

import za.co.wethinkcode.robot.server.Map.Obstacle;
import za.co.wethinkcode.robot.server.World;

import java.sql.Connection;
import java.util.Vector;

public interface Persistence {


    void createDatabase(World world);

    void saveWorld(World world, String name, int size);

    void updateWorld();

    void deleteWorld();

    void readWorld();

    void addObstacleListType(Vector <Obstacle> objects, String type);

    void addAllObstacles(World world);

    void checkWorldNameExistence();

}
