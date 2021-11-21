package za.co.wethinkcode.robot.api;

import io.javalin.Javalin;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;
import org.apache.commons.cli.*;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Server.MultiServer;
import za.co.wethinkcode.robot.server.Utility.ConfigReader;
import za.co.wethinkcode.robot.server.World;

public class WorldApiServer {

    public static Option port;
    public static Option size;
    public static Option obstacle;
    public static CommandLine cmd;
    public static Options options = new Options();
    public static int worldSize = 1;
    public static ConfigReader config = new ConfigReader();
    public final Javalin server;

    public WorldApiServer(World world, String dbUrl){
        server = Javalin.create(config -> {
            config.defaultContentType = "application/json";
            config.registerPlugin(new OpenApiPlugin(getApiOptions()));
        });
        WorldApiHandler worldApiHandler = new WorldApiHandler(dbUrl);

        this.server.get("/world", context -> worldApiHandler.getObstaclesFromCurrentWorld(context, world));
        this.server.get("/world/{world}", context -> worldApiHandler.getObstaclesFromDatabase(context));
        this.server.post("/robot/{name}", context -> worldApiHandler.launchRobotCommand(context, world));
        this.server.get("/admin/robots", context -> worldApiHandler.getListOfRobots(context, world));
        this.server.delete("/admin/robot/{name}", context -> worldApiHandler.killRobot(context, world));
        this.server.post("/admin/obstacles", context -> worldApiHandler.addObstaclesToWorld(context, world));
        this.server.delete("/admin/obstacles", context -> worldApiHandler.deleteListOfObstacles(context, world));
    }

    private OpenApiOptions getApiOptions(){
        Info applicationInfo = new Info()
                .version("1.3")
                .description("My Application");
        return new OpenApiOptions(applicationInfo).path("/swagger-docs")
                .swagger(new SwaggerOptions("/swagger")
                        .title("Uss Victory API Documentation"));
    }

    public static void main(String[] args) {
        //Create command line argument options
        {
            port = new Option("p", "port", true, "Server port number to listen on");
            size = new Option("s", "size", true, "Size of the world");
            obstacle = new Option("o", "obstacle", true, "Place an obstacle in the world");
        }
        //Add options
        {
            options.addOption(port);
            options.addOption(size);
            options.addOption(obstacle);
        }

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        try {
            cmd = parser.parse(options, args);

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Available commands", options);
            System.exit(1);
            return;
        }

        if (cmd.getOptionValue("size") != null) {
            worldSize = Integer.parseInt(cmd.getOptionValue("size"));
        }

            Position BOTTOM_RIGHT = new Position((worldSize / 2), (-worldSize / 2));
            Position TOP_LEFT = new Position((-worldSize / 2), (worldSize / 2));

            int port = 5000;
            if (cmd.getOptionValue("port") != null) {
                port = Integer.parseInt(cmd.getOptionValue("port"));
            }

            //Checks to see if an argument was provided for determining what maze to use.
            boolean specifiedObstacle = false;
            if (cmd.getOptionValue("obstacle") != null) {
                specifiedObstacle = true;
            }

            World world;

            world = new World(config.getMap(), BOTTOM_RIGHT, TOP_LEFT, MultiServer.getObstaclePosition(cmd.getOptionValue("obstacle")), specifiedObstacle);

            //Print world size
            System.out.println("World Size: " + worldSize + "x" + worldSize);
            System.out.println("Port: " + port);
            System.out.println("Obstacles: " + world.getObstacles().size());
            System.out.println("Server running & waiting for client connections.");

            WorldApiServer server = new WorldApiServer(world, "jdbc:sqlite:uss_victory_db.sqlite");
            server.start(port);
    }

    public void start(int port){
        this.server.start(port);
    }

    public void stop(){
        this.server.stop();
    }
}
