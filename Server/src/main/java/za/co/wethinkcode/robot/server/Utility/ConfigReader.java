package za.co.wethinkcode.robot.server.Utility;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class ConfigReader {
    private int width,height,visibility, maxShieldStrength, shieldRechargeTime
            ,mineSetTime, reloadTime, port, maxShots;

    public ConfigReader() {
        try {
            // Create new properties object to store config in.
            Properties prop = new Properties();
            //use this to load the properties file
            FileInputStream file;

            //the base folder is ./, the root of the config.properties file
            String path = "./config.properties";

            //load the file handle for config.properties
            try {
                file = new FileInputStream(path);
            } catch (FileNotFoundException e) {
                CreateConfig newFile = new CreateConfig();
                newFile.createConfigFile();
                file = new FileInputStream(path);
            }
            prop.load(file);


            // get the property value and store them in fields of the class.
            this.width = Integer.parseInt(prop.getProperty("width"));
            this.height = Integer.parseInt(prop.getProperty("height"));
            this.visibility = Integer.parseInt(prop.getProperty("visibility"));
            this.maxShieldStrength = Integer.parseInt(prop.getProperty("max_shield_strength"));
            this.shieldRechargeTime = Integer.parseInt(prop.getProperty("shield_recharge_time"));
            this.mineSetTime = Integer.parseInt(prop.getProperty("mine_set_time"));
            this.reloadTime = Integer.parseInt(prop.getProperty("reload_time"));
            this.port = Integer.parseInt(prop.getProperty("port"));
            this.maxShots = Integer.parseInt(prop.getProperty("max_shots"));

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    public int getMaxShots() {
        return this.maxShots;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return height;
    }

    public int getVisibility() {
        return visibility;
    }

    public int getReloadTime() {
        return reloadTime;
    }

    public int getMaxShieldStrength() {
        return maxShieldStrength;
    }

    public int getShieldRechargeTime() {
        return shieldRechargeTime;
    }

    public int getMineSetTime() {
        return mineSetTime;
    }

    public int getPort() {
        return port;
    }
}
