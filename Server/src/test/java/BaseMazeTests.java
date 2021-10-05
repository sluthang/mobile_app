import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.Vector;

import za.co.wethinkcode.robot.server.Map.BaseMaze;
import za.co.wethinkcode.robot.server.Map.Obstacle;
import za.co.wethinkcode.robot.server.Robot.Position;

import static org.junit.jupiter.api.Assertions.*;

public class BaseMazeTests {

    @Test
    void setObastaclesTest(){
        Vector<Obstacle> obstaclesList = new Vector<>();
        BaseMaze maze = new BaseMaze();
        maze.createObstacles(new Position(1,1));

        assertNotEquals(maze.getObstacles(), obstaclesList);

        maze.setObstaclesList();
        assertEquals(maze.getObstacles(), obstaclesList);
    }

    @Test
    void  setMinesTest(){
        Vector<Obstacle> minesList = new Vector<>();
        BaseMaze maze = new BaseMaze();
        assertEquals(maze.getMines(), minesList);
    }

    @Test
    void  setPitsTest(){
        Vector<Obstacle> pitsList = new Vector<>();
        BaseMaze maze = new BaseMaze();
        assertEquals(maze.getMines(), pitsList);
    }
}
