import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import za.co.wethinkcode.server.robotclient.RobotWorldClient;
import za.co.wethinkcode.server.robotclient.RobotWorldJsonClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TwoByTwoWorldWithObstacleTests {
    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "127.0.0.1";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();
    private final RobotWorldClient serverClientTwo = new RobotWorldJsonClient();
    private final RobotWorldClient serverClientThree = new RobotWorldJsonClient();
    private final RobotWorldClient serverClientFour = new RobotWorldJsonClient();

    @BeforeEach
    public void connectToServer() {
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        serverClientTwo.connect(DEFAULT_IP, DEFAULT_PORT);
        serverClientThree.connect(DEFAULT_IP, DEFAULT_PORT);
        serverClientFour.connect(DEFAULT_IP, DEFAULT_PORT);
    }

    @AfterEach
    public void disconnectFromServer() {
        serverClient.disconnect();
        serverClientTwo.disconnect();
        serverClientThree.disconnect();
        serverClientFour.disconnect();
    }

    @Test
    public void lookAndFindingObstacle() {
        boolean loop = true;
        //We loop because the robots spawn points on our server are random
        while (loop) {
            disconnectFromServer();
            connectToServer();

            // Given that I am connected to a running Robot Worlds server
            // And the world is of size 2x2 (The world is configured or hardcoded to this size)

            assertTrue(serverClient.isConnected());

            // When I send a launch command
            String requestHal = "{" +
                    "  \"robot\": \"HAL\"," +
                    "  \"command\": \"launch\"," +
                    "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                    "}";

            JsonNode responseHal = serverClient.sendRequest(requestHal);

            // Then I should get an "OK" response
            assertNotNull(responseHal.get("result"));
            assertEquals("OK", responseHal.get("result").asText());

            // And I issue a state command

            String stateRequest = " {\"robot\":\"HAL\"," +
                    "\"arguments\":[]," +
                    "\"command\":\"look" +
                    "\"}";

            JsonNode stateResponse = serverClient.sendRequest(stateRequest);

            if (!stateResponse.get("data").get("objects").toString().contains("OBSTACLE")) {
                disconnectFromServer();
                connectToServer();
            } else {
                loop = false;
                int counter = 0;

                for(int i = 0; i<=stateResponse.get("data").get("objects").size();i++){
                    if(stateResponse.get("data").get("objects").get(i).get("type").asText().equalsIgnoreCase("OBSTACLE")){
                        counter = i;
                        break;
                    }
                }

                assertTrue(stateResponse.get("data").get("objects").get(counter).toString().contains("OBSTACLE"));
                assertTrue(stateResponse.get("data").get("objects").get(counter).toString().contains("1"));
            }
        }
    }

    @Test
    public void launchRobotsIntoAWorldWithAnObstacle(){
           //Given a world of size 2x2
          //and the world has an obstacle at coordinate [1,1]

        assertTrue(serverClient.isConnected());
        assertTrue(serverClientTwo.isConnected());
        assertTrue(serverClientThree.isConnected());

        // When I launch 3 robots into the world
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

        String requestR2D = "{" +
                "  \"robot\": \"R2D\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        JsonNode responseHal = serverClient.sendRequest(requestHal);
        JsonNode responseR2D2 = serverClientTwo.sendRequest(requestR2D2);
        JsonNode responseR2D = serverClientThree.sendRequest(requestR2D);

        assertNotNull(responseHal.get("result"));
        assertNotNull(responseR2D2.get("result"));
        assertNotNull(responseR2D.get("result"));


        assertEquals("OK", responseHal.get("result").asText());
        assertEquals("OK", responseR2D2.get("result").asText());
        assertEquals("OK", responseR2D.get("result").asText());

        //Then each robot cannot be in position [1,1].
        assertNotNull(responseHal.get("data"));
        assertNotNull(responseHal.get("data").get("position"));

        assertNotNull(responseR2D2.get("data"));
        assertNotNull(responseR2D2.get("data").get("position"));

        assertNotNull(responseR2D.get("data"));
        assertNotNull(responseR2D.get("data").get("position"));


    }
    @Test
    public void worldWithAnObstacleIsFull() {

//        Given a world of size 2x2
//        and the world has an obstacle at coordinate [1,1]
//        and I have successfully launched 3 robots into the world
//        When I launch one more robot
//        Then I should get an error response back with the message "No more space in this world"

        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 2x2 (The world is configured or hardcoded to this size)

        assertTrue(serverClient.isConnected());
        assertTrue(serverClientTwo.isConnected());
        assertTrue(serverClientThree.isConnected());
        assertTrue(serverClientFour.isConnected());

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

        String requestR2D = "{" +
                "  \"robot\": \"R2D\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        String requestR2 = "{" +
                "  \"robot\": \"R2\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";


        JsonNode responseHal = serverClient.sendRequest(requestHal);
        JsonNode responseR2D2 = serverClientTwo.sendRequest(requestR2D2);
        JsonNode responseR2D = serverClientThree.sendRequest(requestR2D);
        JsonNode responseR2 = serverClientFour.sendRequest(requestR2);

        // Then I should get an "OK" response
        assertNotNull(responseHal.get("result"));
        assertNotNull(responseR2D2.get("result"));
        assertNotNull(responseR2D.get("result"));
        assertNotNull(responseR2.get("result"));
        
        assertEquals("OK", responseHal.get("result").asText());
        assertEquals("OK", responseR2D2.get("result").asText());
        assertEquals("OK", responseR2D.get("result").asText());
        assertEquals("ERROR", responseR2.get("result").asText());

        assertEquals(responseR2.get("data").get("message").asText() , "No more space in this world");

    }
}
