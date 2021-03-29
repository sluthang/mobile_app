package server.Map;

import za.co.wethinkcode.robot.server.Robot.Position;

public class SimpleMaze extends BaseMaze {

    /**
     * Simple maze constructor, sets up 1 obstacle at position 1,1
     * */
    public SimpleMaze() {
        createObstacles(new Position(1, 1));
    }
}