package za.co.wethinkcode.robot.server.Utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateConfig {

    public void createConfigFile() {
        try {
            File newFile = new File("./config.properties");
            if (newFile.createNewFile()) {
                System.out.println("config file created: " + newFile.getName());
                try {
                    FileWriter writer = new FileWriter("config.properties");
                    writer.write("ip=192.168.0.1\n" +
                            "port=6969\n" +
                            "width=70\n" +
                            "height=40\n" +
                            "visibility=10\n" +
                            "map=RandomMaze\n" +
                            "max_shield_strength=5\n" +
                            "max_shots=5\n" +
                            "shield_recharge_time=10\n" +
                            "mine_set_time=10\n" +
                            "reload_time=10");
                    writer.close();
                    System.out.println("Successfully added default values.\n" +
                            "If you would like to edit the config, edit the config.properties file and relaunch.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("config file already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
