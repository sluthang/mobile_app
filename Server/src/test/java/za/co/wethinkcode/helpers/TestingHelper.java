package za.co.wethinkcode.helpers;

import org.apache.commons.lang3.RandomStringUtils;
import za.co.wethinkcode.server.robotclient.RobotWorldJsonClient;

import java.util.ArrayList;

public class TestingHelper {

    public String generateRandomRobotName(){
        return RandomStringUtils.randomAlphabetic(10);
    }


    /**
     * Add new launch command json string to list
     * @param name
     */
    public String addLaunchJsonString(String name){

        String newCommand = "{" +
                "  \"robot\": " +
                "\"" +name+"\"" + "," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        return newCommand;
    }

    public ArrayList<String> generateLaunchCommands(int numberOfLaunchJsonStrings){
        ArrayList<String> launchCommandStringList = new ArrayList<>();

        for (int i = 1; i <= numberOfLaunchJsonStrings; i++){
            launchCommandStringList.add(addLaunchJsonString(generateRandomRobotName()));
        }
        return launchCommandStringList;
    }

    /**
     * Generate multiple <RobotWorldJsonClient> objects
     * @param number
     * @return
     */
    public ArrayList<RobotWorldJsonClient> createConnections(int number){
        ArrayList<RobotWorldJsonClient> connections = new ArrayList<>();

        for (int i = 0; i <= number; i++){
            connections.add(new RobotWorldJsonClient());
        }
        return connections;
    }

    public void connectMultipleRobotClientObjects(ArrayList<RobotWorldJsonClient> connectionList, int port, String ip){
        for (RobotWorldJsonClient connection: connectionList){
            connection.connect(ip, port);
        }
    }

    public void disconnectMultipleRobotClientObjects(ArrayList<RobotWorldJsonClient> connectionList){
        for (RobotWorldJsonClient connection: connectionList){
            connection.disconnect();
        }
    }
}
