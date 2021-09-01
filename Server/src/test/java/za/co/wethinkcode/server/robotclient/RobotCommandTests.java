package za.co.wethinkcode.server.robotclient;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RobotCommandTests {

    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "127.0.0.1";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();

    @BeforeEach
    public void connectToServer(){
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
    }

    @AfterEach
    public void disconnectFromServer(){
        serverClient.disconnect();
    }

    @Test
    public void validLaunchShouldSucceed() {

        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);

        assertTrue(serverClient.isConnected());

        // When I send a valid launch request to the server
        String launchRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        JsonNode response = serverClient.sendRequest(launchRequest);

        // Then I should get a valid response from the server
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        //Sending a look request after a successful launch.
        String lookRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"look\"," +
                "  \"arguments\": []" +
                "}";

        JsonNode lookResponse = serverClient.sendRequest(lookRequest);
        String serverResponse = lookResponse.get("data").get("objects").toString();

        assertTrue(serverResponse.contains("\"direction\":\"SOUTH\"") && serverResponse.contains("\"type\":\"EDGE\"") && serverResponse.contains("\"distance\":1"));
        assertTrue(serverResponse.contains("\"direction\":\"NORTH\"") && serverResponse.contains("\"type\":\"EDGE\"") && serverResponse.contains("\"distance\":1"));
        assertTrue(serverResponse.contains("\"direction\":\"WEST\"") && serverResponse.contains("\"type\":\"EDGE\"") && serverResponse.contains("\"distance\":1"));
        assertTrue(serverResponse.contains("\"direction\":\"EAST\"") && serverResponse.contains("\"type\":\"EDGE\"") && serverResponse.contains("\"distance\":1"));
    }
}
