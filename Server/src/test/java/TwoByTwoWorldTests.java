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
    public void launchAnotherRobot(){
        // Given a world of size 2 x 2
        assertTrue(serverClient.isConnected());
        assertTrue(serverClientTwo.isConnected());

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

        JsonNode responseFirstRobot = serverClient.sendRequest(requestFirstRobot);
        JsonNode responseSecondRobot = serverClientTwo.sendRequest(requestSecondRobot);

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

}
