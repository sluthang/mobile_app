import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import za.co.wethinkcode.server.robotclient.RobotWorldClient;
import za.co.wethinkcode.server.robotclient.RobotWorldJsonClient;

import static org.junit.jupiter.api.Assertions.*;

public class TwoByTwoWorldWithObstacleTests {
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

    @Test
    public void launchRobotsIntoAWorldWithAnObstacle(){
           //Given a world of size 2x2
          //and the world has an obstacle at coordinate [1,1]

        assertTrue(serverClient.isConnected());
        assertTrue(serverClientTwo.isConnected());
        assertTrue(serverClientThree.isConnected());
        assertTrue(serverClientFour.isConnected());
        assertTrue(serverClientFive.isConnected());
        assertTrue(serverClientSix.isConnected());
        assertTrue(serverClientSeven.isConnected());
        assertTrue(serverClientEight.isConnected());


        // When I launch 8 robots into the world
        String requestHal = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        String requestSkiper = "{" +
                "  \"robot\": \"Skiper\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        String requestDelta = "{" +
                "  \"robot\": \"Delta\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        String requestTango = "{" +
                "  \"robot\": \"Tango\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        String requestRoger = "{" +
                "  \"robot\": \"Roger\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        String requestX0 = "{" +
                "  \"robot\": \"X0\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        String requestX1 = "{" +
                "  \"robot\": \"X1\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        String requestX2 = "{" +
                "  \"robot\": \"X2\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        JsonNode responseHal = serverClient.sendRequest(requestHal);
        JsonNode responseSkiper = serverClientTwo.sendRequest(requestSkiper);
        JsonNode responseDelta = serverClientThree.sendRequest(requestDelta);
        JsonNode responseTango = serverClientFour.sendRequest(requestTango);
        JsonNode responseRoger = serverClientFive.sendRequest(requestRoger);
        JsonNode responseX0 = serverClientSix.sendRequest(requestX0);
        JsonNode responseX1 = serverClientSeven.sendRequest(requestX1);
        JsonNode responseX2 = serverClientEight.sendRequest(requestX2);


        assertNotNull(responseHal.get("result"));
        assertNotNull(responseSkiper.get("result"));
        assertNotNull(responseDelta.get("result"));
        assertNotNull(responseTango.get("result"));
        assertNotNull(responseRoger.get("result"));
        assertNotNull(responseX0.get("result"));
        assertNotNull(responseX1.get("result"));
        assertNotNull(responseX2.get("result"));


        assertEquals("OK", responseHal.get("result").asText());
        assertEquals("OK", responseSkiper.get("result").asText());
        assertEquals("OK", responseDelta.get("result").asText());
        assertEquals("OK", responseTango.get("result").asText());
        assertEquals("OK", responseRoger.get("result").asText());
        assertEquals("OK", responseX0.get("result").asText());
        assertEquals("OK", responseX1.get("result").asText());
        assertEquals("OK", responseX2.get("result").asText());

        //Then each robot cannot be in position [1,1].
        assertNotNull(responseHal.get("data"));
        assertNotNull(responseHal.get("data").get("position"));
        assertNotEquals("[1,1]",responseHal.get("data").get("position").asText());

        assertNotNull(responseSkiper.get("data"));
        assertNotNull(responseSkiper.get("data").get("position"));
        assertNotEquals("[1,1]",responseSkiper.get("data").get("position").asText());

        assertNotNull(responseDelta.get("data"));
        assertNotNull(responseDelta.get("data").get("position"));
        assertNotEquals("[1,1]",responseDelta.get("data").get("position").asText());

        assertNotNull(responseTango.get("data"));
        assertNotNull(responseTango.get("data").get("position"));
        assertNotEquals("[1,1]",responseTango.get("data").get("position").asText());

        assertNotNull(responseRoger.get("data"));
        assertNotNull(responseRoger.get("data").get("position"));
        assertNotEquals("[1,1]",responseRoger.get("data").get("position").asText());

        assertNotNull(responseX0.get("data"));
        assertNotNull(responseX0.get("data").get("position"));
        assertNotEquals("[1,1]",responseX0.get("data").get("position").asText());

        assertNotNull(responseX1.get("data"));
        assertNotNull(responseX1.get("data").get("position"));
        assertNotEquals("[1,1]",responseX1.get("data").get("position").asText());

        assertNotNull(responseX2.get("data"));
        assertNotNull(responseX2.get("data").get("position"));
        assertNotEquals("[1,1]",responseX2.get("data").get("position").asText());

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
