import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import za.co.wethinkcode.helpers.TestingHelper;
import za.co.wethinkcode.server.robotclient.RobotWorldJsonClient;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LookWithObstacleAtZeroOne {

    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "127.0.0.1";
    private final TestingHelper helper = new TestingHelper();
    private final ArrayList<RobotWorldJsonClient> eightConnections = helper.createConnections(8);

    @BeforeEach
    public void connectToServer() {
        helper.connectMultipleRobotClientObjects(eightConnections, DEFAULT_PORT, DEFAULT_IP);
    }

    @AfterEach
    public void disconnectFromServer() {
        helper.disconnectMultipleRobotClientObjects(eightConnections);
    }

    @Test
    public void lookAndFindingObstacle() {

        //We loop because the robots spawn points on our server are random
        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 2x2 (The world is configured or hardcoded to this size)
        //and the world has an obstacle at coordinate [0,1]

        assertTrue(eightConnections.get(0).isConnected());

        // When I send a launch command
        String requestHal = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        JsonNode one = eightConnections.get(0).sendRequest(requestHal);

        // Then I should get an "OK" response
        assertNotNull(one.get("result"));
        assertEquals("OK", one.get("result").asText());

        // And I issue a state command

        String stateRequest = " {\"robot\":\"HAL\"," +
                "\"arguments\":[]," +
                "\"command\":\"look" +
                "\"}";

        JsonNode stateResponse = eightConnections.get(0).sendRequest(stateRequest);


        assertTrue(stateResponse.get("data").get("objects").get(0).toString().contains("OBSTACLE"));
        assertTrue(stateResponse.get("data").get("objects").get(0).toString().contains("1"));
    }

    @Test
    public void lookAndFindingOtherRobotsAndObstacles() {

        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 2x2 (The world is configured or hardcoded to this size)
        //and the world has an obstacle at coordinate [0,1]

        // When I send a launch command
        String requestHal = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        ArrayList<String> launchCommandJsonStrings = helper.generateLaunchCommands(7);

        JsonNode one = eightConnections.get(0).sendRequest(requestHal);
        JsonNode two = eightConnections.get(1).sendRequest(launchCommandJsonStrings.get(0));
        JsonNode three = eightConnections.get(2).sendRequest(launchCommandJsonStrings.get(1));
        JsonNode four = eightConnections.get(3).sendRequest(launchCommandJsonStrings.get(2));
        JsonNode five = eightConnections.get(4).sendRequest(launchCommandJsonStrings.get(3));
        JsonNode six = eightConnections.get(5).sendRequest(launchCommandJsonStrings.get(4));
        JsonNode seven = eightConnections.get(6).sendRequest(launchCommandJsonStrings.get(5));
        JsonNode eight = eightConnections.get(7).sendRequest(launchCommandJsonStrings.get(6));


        // Then I should get an "OK" response
        assertEquals("OK", one.get("result").asText());
        assertEquals("OK", two.get("result").asText());
        assertEquals("OK", three.get("result").asText());
        assertEquals("OK", four.get("result").asText());
        assertEquals("OK", five.get("result").asText());
        assertEquals("OK", six.get("result").asText());
        assertEquals("OK", seven.get("result").asText());
        assertEquals("OK", eight.get("result").asText());


        // And I issue a state command
        String stateRequest = " {\"robot\":\"HAL\"," +
                "\"arguments\":[]," +
                "\"command\":\"look" +
                "\"}";

        JsonNode stateResponse = eightConnections.get(0).sendRequest(stateRequest);
        assertTrue(stateResponse.get("data").get("objects").toString().contains("OBSTACLE"));
    }
}
