package za.co.wethinkcode.robot.server.Map;


import za.co.wethinkcode.robot.server.Robot.Position;

@SuppressWarnings("unused")
public class EmptyMaze extends BaseMaze {

    /**
     * Constructor for the empty maze.
     * it has a string whatIDo which is an easter EGG!!! Please don't mark me down.
     * */
    public EmptyMaze(Position position, boolean specified) {
        String whatIDo = "I do nothing :)!";
        addSpecifiedObstacle(specified, position);
    }
}