import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robot.server.Commands.ForwardCommand;
import za.co.wethinkcode.robot.server.Robot.Direction;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ForwardCommandTests {
    ForwardCommand forwardCommand = new ForwardCommand("forward");

    @Test
    public void setMessageAtEdgeNorth(){
        assertEquals("At the NORTH edge", forwardCommand.setMessageAtEdge(Direction.NORTH));
    }

    @Test
    public void setMessageAtEdgeSouth(){
        assertEquals("At the SOUTH edge", forwardCommand.setMessageAtEdge(Direction.SOUTH));
    }

    @Test
    public void setMessageAtEdgeEast(){
        assertEquals("At the EAST edge", forwardCommand.setMessageAtEdge(Direction.EAST));
    }

    @Test
    public void setMessageAtEdgeWest(){
        assertEquals("At the WEST edge", forwardCommand.setMessageAtEdge(Direction.WEST));
    }

}
