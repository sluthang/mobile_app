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
                    writer.write("port=5000\n" +
                            "world_size=1\n" +
                            "width=1\n" +
                            "height=1\n" +
                            "visibility=10\n" +
                            "map=emptymaze\n" +
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
