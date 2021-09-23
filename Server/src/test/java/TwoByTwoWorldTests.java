import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import za.co.wethinkcode.helpers.TestingHelper;
import za.co.wethinkcode.server.robotclient.RobotWorldClient;
import za.co.wethinkcode.server.robotclient.RobotWorldJsonClient;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TwoByTwoWorldTests {

    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "127.0.0.1";
    private TestingHelper helper = new TestingHelper();
    private final ArrayList<RobotWorldJsonClient> twoConnectionsList = helper.createConnections(2);
    private final ArrayList<RobotWorldJsonClient> nineConnectionsList = helper.createConnections(10);


    @BeforeEach
    public void connectToServer(){
        helper.connectMultipleRobotClientObjects(twoConnectionsList, DEFAULT_PORT, DEFAULT_IP);
        helper.connectMultipleRobotClientObjects(nineConnectionsList, DEFAULT_PORT, DEFAULT_IP);
    }

    @AfterEach
    public void disconnectFromServer(){
        helper.disconnectMultipleRobotClientObjects(twoConnectionsList);
        helper.disconnectMultipleRobotClientObjects(nineConnectionsList);
    }

    @Test
    public void launchAnotherRobot(){
        // Given a world of size 2 x 2
//        helper.connectMultipleRobotClientObjects(twoConnectionsList, DEFAULT_PORT, DEFAULT_IP);

        assertTrue(twoConnectionsList.get(0).isConnected());
        assertTrue(twoConnectionsList.get(1).isConnected());

        // and robot "HAL" has already been launched into the world
        String requestFirstRobot = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        // When I launch robot "R2D2" into the world
        String requestSecondRobot = "{" +
                "  \"robot\": \"R2D2\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"sniper\",\"5\",\"5\"]" +
                "}";

        JsonNode responseFirstRobot = twoConnectionsList.get(0).sendRequest(requestFirstRobot);
        JsonNode responseSecondRobot = twoConnectionsList.get(1).sendRequest(requestSecondRobot);

        // Then the launch should be successful
        assertNotNull(responseFirstRobot.get("result"));
        assertNotNull(responseSecondRobot.get("result"));
        assertEquals("OK",responseFirstRobot.get("result").asText());
        assertEquals("OK",responseSecondRobot.get("result").asText());

        assertNotNull(responseFirstRobot.get("data"));
        assertNotNull(responseFirstRobot.get("data").get("position"));

        // and a randomly allocated position of R2D2 should be returned.
        assertNotNull(responseSecondRobot.get("data"));
        assertNotNull(responseSecondRobot.get("data").get("position"));

    }

    @Test
    public void worldWithAnObstacleIsFull() {

//        Given a world of size 2x2
//        and I have successfully launched 9 robots into the world
//        When I launch one more robot
//        Then I should get an error response back with the message "No more space in this world"

        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 2x2 (The world is configured or hardcoded to this size)


        for (int i = 1; i < nineConnectionsList.size(); i++){
            assertTrue(nineConnectionsList.get(i).isConnected());
        }

        ArrayList<String> launchCommandJsonStrings = helper.generateLaunchCommands(10);

        JsonNode one = nineConnectionsList.get(0).sendRequest(launchCommandJsonStrings.get(0));
        JsonNode two = nineConnectionsList.get(1).sendRequest(launchCommandJsonStrings.get(1));
        JsonNode three = nineConnectionsList.get(2).sendRequest(launchCommandJsonStrings.get(2));
        JsonNode four = nineConnectionsList.get(3).sendRequest(launchCommandJsonStrings.get(3));
        JsonNode five = nineConnectionsList.get(4).sendRequest(launchCommandJsonStrings.get(4));
        JsonNode six = nineConnectionsList.get(5).sendRequest(launchCommandJsonStrings.get(5));
        JsonNode seven = nineConnectionsList.get(6).sendRequest(launchCommandJsonStrings.get(6));
        JsonNode eight = nineConnectionsList.get(7).sendRequest(launchCommandJsonStrings.get(7));
        JsonNode nine = nineConnectionsList.get(8).sendRequest(launchCommandJsonStrings.get(8));
        JsonNode ten = nineConnectionsList.get(9).sendRequest(launchCommandJsonStrings.get(9));

        // Then I should get an "OK" response
        assertEquals("OK", one.get("result").asText());
        assertEquals("OK", two.get("result").asText());
        assertEquals("OK", three.get("result").asText());
        assertEquals("OK", four.get("result").asText());
        assertEquals("OK", five.get("result").asText());
        assertEquals("OK", six.get("result").asText());
        assertEquals("OK", seven.get("result").asText());
        assertEquals("OK", eight.get("result").asText());
        assertEquals("OK", nine.get("result").asText());
        assertEquals("ERROR", ten.get("result").asText());

        assertEquals(ten.get("data").get("message").asText() , "No more space in this world");

    }
}
