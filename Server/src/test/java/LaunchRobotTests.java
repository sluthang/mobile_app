import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import za.co.wethinkcode.server.robotclient.RobotWorldClient;
import za.co.wethinkcode.server.robotclient.RobotWorldJsonClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * As a player
 * I want to launch my robot in the robot world
 * So that I can annihilate everyone on the robot world
 */

public class LaunchRobotTests {

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
    public void validLaunchShouldSucceed(){

        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);

        assertTrue(serverClient.isConnected());

        // When I send a valid launch request to the server
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a valid response from the server
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        // And the position should be (x:0, y:0)
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("position"));
        assertEquals(0, response.get("data").get("position").get(0).asInt());
        assertEquals(0, response.get("data").get("position").get(1).asInt());

        // And I should also get the state of the robot
        assertNotNull(response.get("state"));
    }

    @Test
    void invalidLaunchShouldFail(){
        // Given that I am connected to a running Robot Worlds server
        Assert.assertTrue(serverClient.isConnected());

        // When I send a invalid launch request with the command "luanch" instead of "launch"
        String request = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"luanch\"," +
                "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get an error response
        Assert.assertNotNull(response.get("result"));
        Assert.assertEquals("ERROR", response.get("result").asText());

        // And the message "Unsupported command"
        Assert.assertNotNull(response.get("data"));
        Assert.assertNotNull(response.get("data").get("message"));
        Assert.assertTrue(response.get("data").get("message").asText().contains("Unsupported command"));
    }

    @Test
    public void invalidLaunchExistingRobotName() {

        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)

        RobotWorldClient secondClient = new RobotWorldJsonClient();
        secondClient.connect(DEFAULT_IP, DEFAULT_PORT);

        assertTrue(serverClient.isConnected());

        // When I send a launch command with an existing robot name
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        JsonNode first_response = serverClient.sendRequest(request);
        JsonNode second_response = secondClient.sendRequest(request);

        // Then I should get an "ERROR" response
        assertNotNull(first_response.get("result"));
        assertEquals("OK", first_response.get("result").asText());
        assertEquals("ERROR", second_response.get("result").asText());

        // And the message "Too many of you in this world"
        assertTrue(second_response.get("data").get("message").asText().contains("Too many of you in this world"));

        secondClient.disconnect();
    }

    @Test
    public void invalidLaunchNotEnoughSpace() {

        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)

        RobotWorldClient secondClient = new RobotWorldJsonClient();
        secondClient.connect(DEFAULT_IP, DEFAULT_PORT);

        assertTrue(serverClient.isConnected());

        // When I send a launch command with an existing robot name
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        String requestTwo = "{" +
                "  \"robot\": \"BOB\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        JsonNode first_response = serverClient.sendRequest(request);
        JsonNode second_response = secondClient.sendRequest(requestTwo);

        // Then I should get an "ERROR" response
        assertNotNull(first_response.get("result"));
        assertEquals("OK", first_response.get("result").asText());
        assertEquals("ERROR", second_response.get("result").asText());

        // And the message "No more space in this world"
        assertEquals(second_response.get("data").get("message").asText(), "No more space in this world");

        secondClient.disconnect();
    }

    @Test
    public void getStateOfLaunchedRobot() {

        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)

        assertTrue(serverClient.isConnected());

        // When I send a launch command
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        JsonNode response = serverClient.sendRequest(request);

        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        // And I issue a state command

        String stateRequest = " {\"robot\":\"HAL\"," +
                "\"arguments\":[]," +
                "\"command\":\"state" +
                "\"}";
        JsonNode stateResponse = serverClient.sendRequest(stateRequest);
        assertNotNull(stateResponse.get("result"));
        assertNotNull(stateResponse.get("state"));
        assertEquals("OK", stateResponse.get("result").asText());

        assertTrue(stateResponse.get("state").get("direction").asText().contains("NORTH"));
    }


    @Test
    public void getStateOfNoneExistingRobot() {

        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)

        assertTrue(serverClient.isConnected());

        // And I issue a state command with a robot name that isn't launched

        String stateRequest = " {\"robot\":\"HAL\"," +
                "\"arguments\":[]," +
                "\"command\":\"state" +
                "\"}";

        JsonNode stateResponse = serverClient.sendRequest(stateRequest);

        assertNotNull(stateResponse.get("result"));

        // Then I should receive a response with the ERROR result
        assertEquals("ERROR", stateResponse.get("result").asText());

        // And the message on the response should be "Robot does not exist"
        assertEquals(stateResponse.get("data").get("message").asText(), "Robot does not exist");
    }

}
