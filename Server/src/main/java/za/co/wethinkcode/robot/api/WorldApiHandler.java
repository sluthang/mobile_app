package za.co.wethinkcode.robot.api;

import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import za.co.wethinkcode.robot.persistence.Database;
import za.co.wethinkcode.robot.server.Commands.Command;
import za.co.wethinkcode.robot.server.Robot.Robot;
import za.co.wethinkcode.robot.server.World;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class WorldApiHandler {

    private static Database database;

    WorldApiHandler(String dbUrl){
        try {
            database = new Database(dbUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getObstaclesFromDatabase(Context context) throws SQLException {
        String name = context.pathParamAsClass("world", String.class).get();
        context.header("Location", "/world/" + name);
        context.status(HttpCode.OK);
        context.result(database.getWorldObjects(name));
    }

    public static void getObstaclesFromCurrentWorld(Context context, World world){
        world.getMaze().addAllObstacles(world);
        context.result(world.getMaze().getObjects().toString());
        world.getMaze().clearObjects();
    }

    public static void launchRobotCommand(Context context, World world){
        String name = context.pathParamAsClass("name", String.class).get();
        context.header("Location", "/robot/" + name);
        JSONObject request = (JSONObject)JSONValue.parse(context.body());
        Command command = Command.create(request);
        context.status(HttpCode.CREATED);
        context.result(world.handleCommand(command, name));
    }

    public static void getListOfRobots(Context context, World world){
        JSONObject response = new JSONObject();
        JSONArray robots = new JSONArray();
        for (Robot worldRobots : world.getRobots().values()){
            robots.add(worldRobots.getName());
        }
        response.put("robots", robots);
        context.status(HttpCode.OK);
        context.result(String.valueOf(response));
    }

    public static void killRobot(Context context, World world){

    }

    public static void addObstaclesToWorld(Context context, World world){

    }

    public static void deleteListOfObstacles(Context context, World world){

    }

    public static void saveWorldMap(Context context, World world){

    }

    public static void loadAndSetMap(Context context, World world){

    }
}
