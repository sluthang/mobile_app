import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import za.co.wethinkcode.server.robotclient.RobotWorldClient;
import za.co.wethinkcode.server.robotclient.RobotWorldJsonClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TwoByTwoWorldTests {

    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "127.0.0.1";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();
    private final RobotWorldClient serverClientTwo = new RobotWorldJsonClient();

    @BeforeEach
    public void connectToServer(){
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        serverClientTwo.connect(DEFAULT_IP, DEFAULT_PORT);
    }

    @AfterEach
    public void disconnectFromServer(){
        serverClient.disconnect();
        serverClientTwo.disconnect();
    }

    @Test
    public void lookAndFindingAnotherRobot() {

        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 2x2 (The world is configured or hardcoded to this size)

        assertTrue(serverClient.isConnected());
        assertTrue(serverClientTwo.isConnected());

        // When I send a launch command
        String requestHal = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        String requestR2D2 = "{" +
                "  \"robot\": \"R2D2\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        JsonNode responseHal = serverClient.sendRequest(requestHal);
        JsonNode responseR2D2 = serverClientTwo.sendRequest(requestR2D2);

        // Then I should get an "OK" response
        assertNotNull(responseHal.get("result"));
        assertNotNull(responseR2D2.get("result"));
        assertEquals("OK", responseHal.get("result").asText());
        assertEquals("OK", responseR2D2.get("result").asText());

        // And I issue a state command

        String stateRequest = " {\"robot\":\"HAL\"," +
                "\"arguments\":[]," +
                "\"command\":\"look" +
                "\"}";

        JsonNode stateResponse = serverClient.sendRequest(stateRequest);
        assertTrue(stateResponse.get("data").get("objects").toString().contains("ROBOT"));
    }
}
