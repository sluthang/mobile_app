import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import za.co.wethinkcode.server.robotclient.RobotWorldClient;
import za.co.wethinkcode.server.robotclient.RobotWorldJsonClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LookWithObstacleAtZeroOne {

    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "127.0.0.1";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();
    private final RobotWorldClient serverClientTwo = new RobotWorldJsonClient();
    private final RobotWorldClient serverClientThree = new RobotWorldJsonClient();
    private final RobotWorldClient serverClientFour = new RobotWorldJsonClient();
    private final RobotWorldClient serverClientFive = new RobotWorldJsonClient();
    private final RobotWorldClient serverClientSix = new RobotWorldJsonClient();
    private final RobotWorldClient serverClientSeven = new RobotWorldJsonClient();
    private final RobotWorldClient serverClientEight = new RobotWorldJsonClient();

    @BeforeEach
    public void connectToServer() {
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        serverClientTwo.connect(DEFAULT_IP, DEFAULT_PORT);
        serverClientThree.connect(DEFAULT_IP, DEFAULT_PORT);
        serverClientFour.connect(DEFAULT_IP, DEFAULT_PORT);
        serverClientFive.connect(DEFAULT_IP, DEFAULT_PORT);
        serverClientSix.connect(DEFAULT_IP, DEFAULT_PORT);
        serverClientSeven.connect(DEFAULT_IP, DEFAULT_PORT);
        serverClientEight.connect(DEFAULT_IP, DEFAULT_PORT);
    }

    @AfterEach
    public void disconnectFromServer() {
        serverClient.disconnect();
        serverClientTwo.disconnect();
        serverClientThree.disconnect();
        serverClientFour.disconnect();
        serverClientFive.disconnect();
        serverClientSix.disconnect();
        serverClientSeven.disconnect();
        serverClientEight.disconnect();
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
            //and the world has an obstacle at coordinate [0,1]

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
    public void lookAndFindingOtherRobotsAndObstacles() {
        boolean loop = true;

        while(loop){
            disconnectFromServer();
            connectToServer();

            // Given that I am connected to a running Robot Worlds server
            // And the world is of size 2x2 (The world is configured or hardcoded to this size)
            //and the world has an obstacle at coordinate [0,1]

            assertTrue(serverClient.isConnected());
            assertTrue(serverClientTwo.isConnected());
            assertTrue(serverClientThree.isConnected());
            assertTrue(serverClientFour.isConnected());
            assertTrue(serverClientFive.isConnected());
            assertTrue(serverClientSix.isConnected());
            assertTrue(serverClientSeven.isConnected());
            assertTrue(serverClientEight.isConnected());

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

            String requestC3PO = "{" +
                    "  \"robot\": \"C3PO\"," +
                    "  \"command\": \"launch\"," +
                    "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                    "}";

            String requestTinMan = "{" +
                    "  \"robot\": \"TinMan\"," +
                    "  \"command\": \"launch\"," +
                    "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                    "}";

            String requestBlits = "{" +
                    "  \"robot\": \"Blits\"," +
                    "  \"command\": \"launch\"," +
                    "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                    "}";

            String requestRumble = "{" +
                    "  \"robot\": \"Rumble\"," +
                    "  \"command\": \"launch\"," +
                    "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                    "}";

            JsonNode responseHal = serverClient.sendRequest(requestHal);
            JsonNode responseR2D2 = serverClientTwo.sendRequest(requestR2D2);
            JsonNode responseR2D = serverClientThree.sendRequest(requestR2D);
            JsonNode responseR2 = serverClientFour.sendRequest(requestR2);
            JsonNode responseTinMan = serverClientFive.sendRequest(requestTinMan);
            JsonNode responseRumble = serverClientSix.sendRequest(requestRumble);
            JsonNode responseC3PO = serverClientSeven.sendRequest(requestC3PO);
            JsonNode responseBlits = serverClientEight.sendRequest(requestBlits);

            // Then I should get an "OK" response
            assertEquals("OK", responseHal.get("result").asText());
            assertEquals("OK", responseR2D2.get("result").asText());
            assertEquals("OK", responseR2D.get("result").asText());
            assertEquals("OK", responseR2.get("result").asText());
            assertEquals("OK", responseTinMan.get("result").asText());
            assertEquals("OK", responseRumble.get("result").asText());
            assertEquals("OK", responseC3PO.get("result").asText());
            assertEquals("OK", responseBlits.get("result").asText());

            // And I issue a state command

            String stateRequest = " {\"robot\":\"HAL\"," +
                    "\"arguments\":[]," +
                    "\"command\":\"look" +
                    "\"}";

            JsonNode stateResponse = serverClient.sendRequest(stateRequest);

            if(!stateResponse.get("data").get("objects").toString().contains("ROBOT")){
                disconnectFromServer();
                connectToServer();
            }else{
                loop = false;
                int numRobots = 0;
                for(int i=0; i<stateResponse.get("data").get("objects").size(); i++){
                    if(stateResponse.get("data").get("objects").get(i).get("type").asText().equalsIgnoreCase("ROBOT")){
                        numRobots+=1;
                    }
                }
                assertEquals(numRobots, 3);
                assertTrue(stateResponse.get("data").get("objects").toString().contains("OBSTACLE"));
            }
        }
    }
}
