package za.co.wethinkcode.robot.persistence;

import za.co.wethinkcode.robot.server.Map.Obstacle;
import za.co.wethinkcode.robot.server.World;

import java.util.Vector;

public interface Persistence {


    void createDatabase(World world);

    void saveWorld(World world, String name, int size);

    void updateWorld(String name);

    void deleteWorld(String name);

<<<<<<< HEAD
    void readWorld(World world, String name);
=======
    void readWorld(String name);
>>>>>>> 7056246f4d3585d6cdae959fee38fef1782693f5

    void addObstacleListType(Vector <Obstacle> objects, String type);

    void addAllObstacles(World world);

}
