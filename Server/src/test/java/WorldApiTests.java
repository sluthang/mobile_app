import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONArray;
import org.junit.jupiter.api.*;
import kong.unirest.Unirest;
import za.co.wethinkcode.robot.api.WorldApiServer;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.World;

import static org.junit.jupiter.api.Assertions.*;

public class WorldApiTests {


    private static WorldApiServer server;
    private static World world;

    @BeforeAll
    public static void startServer(){
        int worldSize = 4;
        Position BOTTOM_RIGHT = new Position((worldSize/2),(-worldSize/2));
        Position TOP_LEFT = new Position((-worldSize/2),(worldSize/2));
        world = new World("emptymaze", BOTTOM_RIGHT, TOP_LEFT, new Position(1,1), true);
        server = new WorldApiServer(world, "jdbc:sqlite:test_worlds_db.sqlite");
        server.start(6000);
    }

    @AfterAll
    public static void stopServer(){
        server.stop();
    }
    
    @Test
    @DisplayName("GET /world")
    public void getCurrentWorldEndpointTest() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:6000/world").asJson();
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getHeaders().getFirst("Content-Type"));
        JSONArray jsonArray = response.getBody().getArray();
        assertEquals(1, jsonArray.length());
    }

    @Test
    @DisplayName("GET /world/{world}")
    public void getWorldFromDatabaseEndpointTest() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:6000/world/beau").asJson();
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getHeaders().getFirst("Content-Type"));

        JsonNode responseBodyArray = response.getBody();
        JSONArray databaseObjects = (JSONArray) responseBodyArray.getObject().get("objects");

        assertEquals(2, databaseObjects.length());
        assertTrue(databaseObjects.get(0).toString().contains("OBSTACLE"));
        assertTrue(databaseObjects.get(1).toString().contains("PIT"));
    }

    @Test
    @DisplayName("POST /robot/{name}")
    public void launchRobotCommandApiEndpointTest(){
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:6000/robot/HAL")
                .header("Content-Type", "application/json")
                .body("{\"robot\":\"HAL\",\"arguments\":[\"sniper\",\"999\",\"1\"],\"command\":\"launch\"}")
                .asJson();
        assertEquals(201, response.getStatus());

        JsonNode responseData = response.getBody();

        assertNotNull(responseData.getObject());
        assertEquals("OK", responseData.getObject().get("result"));

        HttpResponse<JsonNode> deleteResponse = Unirest.delete("http://localhost:6000/admin/robot/HAL").
                header("Content-Type", "application/json")
                .asJson();
        assertEquals(200, deleteResponse.getStatus());
    }

    @Test
    public void GetListOfRobotsTest(){
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:6000/admin/robots").asJson();

        assertEquals(200, response.getStatus());
        JsonNode responseData = response.getBody();

        assertNotNull(responseData.getObject());
        assertEquals("{\"robots\":[]}", responseData.getObject().toString());

    }

    @Test
    public void addOneRobotAndGetListOfRobotsTest(){

        HttpResponse<JsonNode> launchResponse = Unirest.post("http://localhost:6000/robot/HAL")
                .header("Content-Type", "application/json")
                .body("{\"robot\":\"HAL\",\"arguments\":[\"sniper\",\"999\",\"1\"],\"command\":\"launch\"}")
                .asJson();

        assertEquals(201, launchResponse.getStatus());

        HttpResponse<JsonNode> response = Unirest.get("http://localhost:6000/admin/robots").asJson();

        assertEquals(200, response.getStatus());
        JsonNode responseData = response.getBody();

        assertNotNull(responseData.getObject());
        assertEquals("{\"robots\":[\"HAL\"]}", responseData.getObject().toString());

        HttpResponse<JsonNode> deleteResponse = Unirest.delete("http://localhost:6000/admin/robot/HAL").
                header("Content-Type", "application/json")
                .asJson();
        assertEquals(200, deleteResponse.getStatus());

    }

    @Test
    public void killRobotEndpointTest() {
        HttpResponse<JsonNode> launchResponse = Unirest.post("http://localhost:6000/robot/HAL")
                .header("Content-Type", "application/json")
                .body("{\"robot\":\"HAL\",\"arguments\":[\"sniper\",\"999\",\"1\"],\"command\":\"launch\"}")
                .asJson();

        assertEquals(201, launchResponse.getStatus());

        HttpResponse<JsonNode> deleteResponse = Unirest.delete("http://localhost:6000/admin/robot/HAL").
                header("Content-Type", "application/json")
                .asJson();

        assertNotNull(deleteResponse);
        assertEquals(200, deleteResponse.getStatus());
    }

    @Test
    public void addObstaclesToWorldEndpointTest(){
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:6000/admin/obstacles")
                .header("Content-Type", "application/json")
                .body("{\"objects\":[{\"position\": [2,2],\"type\":\"OBSTACLE\"}]}")
                .asJson();
        assertEquals(201, response.getStatus());
        assertEquals(2, world.getMaze().getObstacles().size());

        HttpResponse<JsonNode> deleteResponse = Unirest.delete("http://localhost:6000/admin/obstacles")
                .header("Content-Type", "application/json")
                .body("{\"objects\":[{\"position\": [2,2],\"type\":\"OBSTACLE\"}]}")
                .asJson();
        assertEquals(200, deleteResponse.getStatus());
    }

    @Test
    public void deleteObstaclesFromWorldEndpointTest(){
        //fix this test
        HttpResponse<JsonNode> response = Unirest.delete("http://localhost:6000/admin/obstacles")
                .header("Content-Type", "application/json")
                .body("{\"objects\":[{\"position\": [1,1],\"type\":\"OBSTACLE\"}]}")
                .asJson();
        assertEquals(200, response.getStatus());
        assertEquals(1, world.getMaze().getObstacles().size());
    }

    @Test
    public void loadWorldMapEndpointTest(){
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:6000/admin/load/beau")
                .header("Content-Type", "application/json")
                .asJson();

        assertEquals(200, response.getStatus());
        assertEquals(1, world.getMaze().getObstacles().size());
        assertEquals(1, world.getMaze().getPits().size());
    }

    @Test
    public void saveWorldMapEndpointTest(){
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:6000/admin/save/test1")
                .header("Content-Type", "application/json")
                .asJson();

        assertEquals(1, world.getMaze().getObstacles().size());
        assertEquals(201, response.getStatus());
    }
}
