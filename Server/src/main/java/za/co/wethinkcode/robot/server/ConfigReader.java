package za.co.wethinkcode.robot.server;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    InputStream inputStream;
    private int width,height,visibility, maxShieldStrength, shieldRechargeTime
            ,mineSetTime, reloadTime, port;
    private String mapName;

    public ConfigReader() {
        try {
            // Create new properties object to store config in.
            Properties prop = new Properties();
            String propFileName = "config.properties";

            // Create an input stream to grab the contents of the .properties file.
            inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(propFileName);

            // Error check the file.
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }


            // get the property value and store them in fields of the class.
            this.width = Integer.parseInt(prop.getProperty("width"));
            this.height = Integer.parseInt(prop.getProperty("height"));
            this.visibility = Integer.parseInt(prop.getProperty("visibility"));
            this.maxShieldStrength = Integer.parseInt(prop.getProperty("max_shield_strength"));
            this.shieldRechargeTime = Integer.parseInt(prop.getProperty("shield_recharge_time"));
            this.mineSetTime = Integer.parseInt(prop.getProperty("mine_set_time"));
            this.reloadTime = Integer.parseInt(prop.getProperty("reload_time"));
            this.port = Integer.parseInt(prop.getProperty("port"));

            this.mapName = prop.getProperty("map");

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {}
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

    public String getMapName() {
        return mapName;
    }

    public int getPort() {
        return port;
    }
}
