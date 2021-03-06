package za.co.wethinkcode.robot.server.Map;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import za.co.wethinkcode.robot.server.Robot.Position;
import za.co.wethinkcode.robot.server.Robot.Robot;
import za.co.wethinkcode.robot.server.Robot.UpdateResponse;
import za.co.wethinkcode.robot.server.World;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class BaseMaze implements Maze {

    Vector<Obstacle> obstaclesList = new Vector<>();
    Vector<Obstacle> pitsList = new Vector<>();
    Vector<Obstacle> minesList = new Vector<>();
    public JSONObject objects = new JSONObject();
    public JSONArray obstacles = new JSONArray();
    public JSONArray jsonObjects = new JSONArray();



    /**
     * setter to set the obstacles.
     * @param obstacle: takes param of a list obstacle.
     * */
    public void setObstacles(Vector<Obstacle> obstacle) {
        this.obstaclesList = obstacle;
    }

    /**
     * getter to get the obstacles.
     * */
    public Vector<Obstacle> getObstacles() {
        return this.obstaclesList;
    }

    public Vector<Obstacle> getPits() {
        return pitsList;
    }

    public Vector<Obstacle> getMines() {
        return minesList;
    }

    public JSONObject getObjects() {
        return this.objects;
    }

    public JSONArray getJsonObjects() {
        return jsonObjects;
    }

    /**
     * Create the obstacle and then add the new obstacle to the list of obstacles.
     * @param position: of the obstacle to be created
     * */
    public void createObstacles(Position position) {
        this.obstaclesList.add(new SquareObstacle(position.getX(), position.getY()));
    }

    /**
     * Creates a new bottomless pit on the map and adds this to the list of current pits.
     * @param position: of the pit to be created
     */
    public void createPit(Position position) {
        this.pitsList.add(new Pits(position.getX(), position.getY()));
    }

    /**
     * Creates a new mine with the position given and adds it to the list of current mines.
     * @param position: of the mine to be created
     */
    public void createMine(Position position) {
        this.minesList.add(new Mines(position.getX(), position.getY()));
    }

    public void deleteObstacle(Position position){
        this.obstaclesList.removeIf(obstacle -> position.getX() == obstacle.getBottomLeftX() && position.getY() == obstacle.getBottomLeftY());
    }

    public void deleteMine(Position position){
        this.minesList.removeIf(obstacle -> position.getX() == obstacle.getBottomLeftX() && position.getY() == obstacle.getBottomLeftY());
    }

    public void deletePit(Position position){
        this.pitsList.removeIf(obstacle -> position.getX() == obstacle.getBottomLeftX() && position.getY() == obstacle.getBottomLeftY());
    }

    /**
     * Takes in 3 parameters, old and new position and checks if the path is blocked per each obstacle in the
     * obstacle lists as well as the list of robots currently in play.
     * @param a : the old position;
     * @param b : the new position;
     * @return: returns UpdateResponse of the obstacle hit.
     * */
    public UpdateResponse blocksPath(Position a, Position b, ConcurrentHashMap<String, Robot> robots, String robotName) {

        int incX = 1;
        int incY = 1;
        if (a.getX() > b.getX()) incX = -1;
        if (a.getY() > b.getY()) incY = -1;

        int x = a.getX();
        do {
            int y = a.getY();
            do {
                for (Obstacle pit : this.pitsList) {
                    if (pit.blocksPosition(new Position(x, y)))
                        return UpdateResponse.FAILED_BOTTOMLESS_PIT;
                }

                for (Obstacle obst : this.obstaclesList) {
                    if (obst.blocksPosition(new Position(x, y))) {
                        return UpdateResponse.FAILED_OBSTRUCTED;
                    }
                }

                for (Obstacle mine : this.minesList) {
                    if (mine.blocksPosition(new Position(x, y))) {
                        return UpdateResponse.FAILED_HIT_MINE;
                    }
                }

                Set<String> keys = robots.keySet();
                for (String key : keys) {
                    if (key.equals(robotName)) {
                        continue;
                    }
                    if (robots.get(key).blocksPosition(new Position(x,y))) {
                        return UpdateResponse.FAILED_OBSTRUCTED;
                    }
                }
                if (y != b.getY()) {
                    y += incY;
                }
            } while (y != b.getY());
            if (x != b.getX()) {
                x += incX;
            }
        } while  (x != b.getX());
        return UpdateResponse.SUCCESS;
    }

    /**
     * Method will loop through the list of mines and check the position of the robot.
     * If the mine is found that the robot has triggered their health will be reduced and the mine will be removed
     * from the list of currently active mines on the field.
     * @param robotPosition position of robot on mine.
//     * @param server of the robot that will be hit by the mine.
     */
    public void hitMine(Position robotPosition, World world, String name) {
        Iterator<Obstacle> i = this.minesList.iterator();

        while (i.hasNext()) {
            Obstacle mine = i.next();
            if (mine.blocksPosition(robotPosition)) {
                world.getRobot(name).takeDamage(3);
                i.remove();
            }
        }
    }

    /**
     * Method will look through all possible lists of Mines,Pits,Robots and obstacles.
     * If any of these obstacles are within this position given the method will return a relevant update response
     * for that obstacle, for example if a mine is at position given FAILED_HIT_MINE is returned.
     * @param robots map of all robots in play.
     * @param position to be checked.
     * @param robotName of robot currently checking for movement.
     * @return UpdateResponse.
     */
    public UpdateResponse blocksPosition(ConcurrentHashMap<String, Robot> robots, Position position, String robotName) {
        if (pitBlockPosition(position) || obstacleBlockPosition(position)) {
            return UpdateResponse.FAILED_OBSTRUCTED;
        }
        else if(mineBlockPosition(position)){
            return UpdateResponse.FAILED_HIT_MINE;
        }
        else if(robotBlockPosition(robots,robotName,position)){
            return UpdateResponse.FAILED_OBSTRUCTED;
        }else{
            return UpdateResponse.SUCCESS;
        }
    }

    /**
     * This method check to see if a pit blocks the position the robot is trying to move to.
     * And returns true or false depending on if the position is blocked or not.
     * @param position  position
     * @return boolean
     */
    private boolean pitBlockPosition(Position position){
        for (Obstacle pit : this.pitsList) {
            if (pit.blocksPosition(position))
                return true;
        }
        return false;
    }

    /**
     * This method check to see if an obstacle blocks the position the robot is trying to move to.
     * And returns true or false depending on if the position is blocked or not.
     * @param position position
     * @return boolean
     */
    private boolean obstacleBlockPosition(Position position){
        for (Obstacle obstacle : this.obstaclesList) {
            if (obstacle.blocksPosition(position))
                return true;
        }
        return false;
    }

    /**
     * This method check to see if a mine blocks the position the robot is trying to move to.
     * And returns true or false depending on if the position is blocked or not.
     * @param position  position
     * @return boolean
     */
    private boolean mineBlockPosition(Position position){
        for (Obstacle mine : this.minesList) {
            if (mine.blocksPosition(position)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method check to see if another robot blocks the position the players' robot is trying to move to.
     * And returns true or false depending on if the position is blocked or not.
     * @param robots Robot
     * @param robotName String
     * @param position Position
     * @return boolean
     */
    private boolean robotBlockPosition(ConcurrentHashMap<String, Robot> robots, String robotName, Position position){
        Set<String> keys = robots.keySet();
        for (String key : keys) {
            if (key.equals(robotName)) {
                continue;
            }
            if (robots.get(key).blocksPosition(position)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add custom obstacle if -o was issued
     * @param specified boolean
     * @param position position
     */
    public void addSpecifiedObstacle(boolean specified, Position position){
        if(specified){
            createObstacles(position);
        }
    }

    /**
     * Resets all the obstacles and pits inside the maze to empty lists/vectors.
     */
    public void resetAllObstacles(){
        this.obstaclesList = new Vector<>();
        this.minesList = new Vector<>();
        this.pitsList = new Vector<>();
    }

    /**
     * Restores a saved worlds obstacles that was stored in a DB.
     * @param data String
     * @throws ParseException exception
     */
    public void restoreAllObstacles(String data) throws ParseException {
        JSONObject jsonObject = stringToJson(data);
        JSONArray jsonArray = (JSONArray) jsonObject.get("objects");
        resetAllObstacles();

        for (Object o : jsonArray) {
            JSONObject json = (JSONObject) o;
            JSONArray position = (JSONArray) json.get("position");
            int x = Integer.parseInt(position.get(0).toString());
            int y = Integer.parseInt(position.get(1).toString());

            switch (json.get("type").toString()) {
                case "OBSTACLE":
                    createObstacles(new Position(x, y));
                    break;
                case "PIT":
                    createPit(new Position(x, y));
                    break;
                case "MINE":
                    createMine(new Position(x, y));
                    break;
            }
        }
    }

    public void addObstacleListType(Vector<Obstacle> objects, String type) {

        for (Obstacle obstacle: objects){
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(obstacle.getBottomLeftX());
            jsonArray.add(obstacle.getBottomLeftY());
            jsonObject.put("type", type);
            jsonObject.put("position", jsonArray);
            this.obstacles.add(jsonObject);
        }
    }

    public void addAllObstacles(World world) {
        addObstacleListType(world.getMaze().getObstacles(), "OBSTACLE");
        addObstacleListType(world.getMaze().getPits(), "PIT");
        addObstacleListType(world.getMaze().getMines(), "MINE");

        for (int i = 0; i < this.obstacles.size(); i++){
            this.objects.put("objects", this.obstacles.get(i));
            this.jsonObjects.add(this.objects.put("objects", this.obstacles.get(i)));
        }
        this.objects.put("objects", this.jsonObjects);
    }

    public void clearObjects(){
        this.obstacles.clear();
        this.objects.clear();

    }

    public void createObjects(JSONObject jsonObject){
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonObject.get("objects");

        for (Object o : jsonArray) {
            JSONObject json = (JSONObject) o;
            JSONArray position = (JSONArray) json.get("position");
            int x = Integer.parseInt(position.get(0).toString());
            int y = Integer.parseInt(position.get(1).toString());

            switch (json.get("type").toString()) {
                case "OBSTACLE":
                    createObstacles(new Position(x, y));
                    break;
                case "PIT":
                    createPit(new Position(x, y));
                    break;
                case "MINE":
                    createMine(new Position(x, y));
                    break;
            }
        }
    }

    public void deleteObjects(JSONObject jsonObject) {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonObject.get("objects");

        for (Object o : jsonArray) {
            JSONObject json = (JSONObject) o;
            JSONArray position = (JSONArray) json.get("position");
            int x = Integer.parseInt(position.get(0).toString());
            int y = Integer.parseInt(position.get(1).toString());

            switch (json.get("type").toString()) {
                case "OBSTACLE":
                    deleteObstacle(new Position(x, y));
                    break;
                case "PIT":
                    deletePit(new Position(x, y));
                    break;
                case "MINE":
                    deleteMine(new Position(x, y));
                    break;
            }
        }
    }

    /**
     * Converts a json string into a json object.
     * @param data String
     * @return JSONObject
     * @throws ParseException exception
     */
    public JSONObject stringToJson(String data) throws ParseException {
        JSONParser parser = new JSONParser();

        return (JSONObject) parser.parse(data);
    }

}