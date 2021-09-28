import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import za.co.wethinkcode.helpers.TestingHelper;
import za.co.wethinkcode.server.robotclient.RobotWorldJsonClient;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class TwoByTwoWorldWithObstacleTests {
    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "127.0.0.1";
    private final TestingHelper helper = new TestingHelper();
    private final ArrayList<RobotWorldJsonClient> nineConnectionsList = helper.createConnections(9);

    @BeforeEach
    public void connectToServer() {
        helper.connectMultipleRobotClientObjects(nineConnectionsList, DEFAULT_PORT, DEFAULT_IP);
    }

    @AfterEach
    public void disconnectFromServer() {
        helper.disconnectMultipleRobotClientObjects(nineConnectionsList);
    }

    @Test
    public void launchRobotsIntoAWorldWithAnObstacle(){
        //Given a world of size 2x2
        //and the world has an obstacle at coordinate [1,1]

        for (int i = 1; i < nineConnectionsList.size(); i++){
            assertTrue(nineConnectionsList.get(i).isConnected());
        }
        
        // When I launch 8 robots into the world
        ArrayList<String> launchCommandJsonStrings = helper.generateLaunchCommands(8);

        JsonNode one = nineConnectionsList.get(0).sendRequest(launchCommandJsonStrings.get(0));
        JsonNode two = nineConnectionsList.get(1).sendRequest(launchCommandJsonStrings.get(1));
        JsonNode three = nineConnectionsList.get(2).sendRequest(launchCommandJsonStrings.get(2));
        JsonNode four = nineConnectionsList.get(3).sendRequest(launchCommandJsonStrings.get(3));
        JsonNode five = nineConnectionsList.get(4).sendRequest(launchCommandJsonStrings.get(4));
        JsonNode six = nineConnectionsList.get(5).sendRequest(launchCommandJsonStrings.get(5));
        JsonNode seven = nineConnectionsList.get(6).sendRequest(launchCommandJsonStrings.get(6));
        JsonNode eight = nineConnectionsList.get(7).sendRequest(launchCommandJsonStrings.get(7));
 


        assertNotNull(one.get("result")); assertNotNull(two.get("result"));
        assertNotNull(three.get("result")); assertNotNull(four.get("result"));
        assertNotNull(five.get("result")); assertNotNull(six.get("result"));
        assertNotNull(seven.get("result")); assertNotNull(eight.get("result"));


        assertEquals("OK", one.get("result").asText());
        assertEquals("OK", two.get("result").asText());
        assertEquals("OK", three.get("result").asText());
        assertEquals("OK", four.get("result").asText());
        assertEquals("OK", five.get("result").asText());
        assertEquals("OK", six.get("result").asText());
        assertEquals("OK", seven.get("result").asText());
        assertEquals("OK", eight.get("result").asText());

        //Then each robot cannot be in position [1,1].
        assertNotNull(one.get("data"));
        assertNotNull(one.get("data").get("position"));
        assertNotEquals("[1,1]",one.get("data").get("position").asText());

        assertNotNull(two.get("data"));
        assertNotNull(two.get("data").get("position"));
        assertNotEquals("[1,1]",two.get("data").get("position").asText());

        assertNotNull(three.get("data"));
        assertNotNull(three.get("data").get("position"));
        assertNotEquals("[1,1]",three.get("data").get("position").asText());

        assertNotNull(four.get("data"));
        assertNotNull(four.get("data").get("position"));
        assertNotEquals("[1,1]",four.get("data").get("position").asText());

        assertNotNull(five.get("data"));
        assertNotNull(five.get("data").get("position"));
        assertNotEquals("[1,1]",five.get("data").get("position").asText());

        assertNotNull(six.get("data"));
        assertNotNull(six.get("data").get("position"));
        assertNotEquals("[1,1]",six.get("data").get("position").asText());

        assertNotNull(seven.get("data"));
        assertNotNull(seven.get("data").get("position"));
        assertNotEquals("[1,1]",seven.get("data").get("position").asText());

        assertNotNull(eight.get("data"));
        assertNotNull(eight.get("data").get("position"));
        assertNotEquals("[1,1]",eight.get("data").get("position").asText());

    }

    @Test
    public void worldWithAnObstacleIsFull() {
        // Given a world of size 2x2
        // and the world has an obstacle at coordinate [1,1]
        // and I have successfully launched 3 robots into the world
        // When I launch one more robot
        // Then I should get an error response back with the message "No more space in this world"

        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 2x2 (The world is configured or hardcoded to this size)

        for (int i = 1; i < nineConnectionsList.size(); i++){
            assertTrue(nineConnectionsList.get(i).isConnected());
        }

        // When I send a launch command
        ArrayList<String> launchCommandJsonStrings = helper.generateLaunchCommands(9);

        JsonNode one = nineConnectionsList.get(0).sendRequest(launchCommandJsonStrings.get(0));
        JsonNode two = nineConnectionsList.get(1).sendRequest(launchCommandJsonStrings.get(1));
        JsonNode three = nineConnectionsList.get(2).sendRequest(launchCommandJsonStrings.get(2));
        JsonNode four = nineConnectionsList.get(3).sendRequest(launchCommandJsonStrings.get(3));
        JsonNode five = nineConnectionsList.get(4).sendRequest(launchCommandJsonStrings.get(4));
        JsonNode six = nineConnectionsList.get(5).sendRequest(launchCommandJsonStrings.get(5));
        JsonNode seven = nineConnectionsList.get(6).sendRequest(launchCommandJsonStrings.get(6));
        JsonNode eight = nineConnectionsList.get(7).sendRequest(launchCommandJsonStrings.get(7));
        JsonNode nine = nineConnectionsList.get(8).sendRequest(launchCommandJsonStrings.get(8));

        // Then I should get an "OK" response
        assertEquals("OK", one.get("result").asText());
        assertEquals("OK", two.get("result").asText());
        assertEquals("OK", three.get("result").asText());
        assertEquals("OK", four.get("result").asText());
        assertEquals("OK", five.get("result").asText());
        assertEquals("OK", six.get("result").asText());
        assertEquals("OK", seven.get("result").asText());
        assertEquals("OK", eight.get("result").asText());
        assertEquals("ERROR", nine.get("result").asText());

        assertEquals(nine.get("data").get("message").asText() , "No more space in this world");

    }
}
