package za.co.wethinkcode.robot.server;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class test {

    public static void main(String[] args) {
        JSONObject thing = (JSONObject) JSONValue.parse(
                " {\"1\":\"One\"," +
                        "\"2\":\"Two\"," +
                        "\"3\":{" +
                        "\"1\":\"bird\"," +
                        "\"2\":\"stone\"," +
                        "\"3\":\"Three\"" +
                        "}}");
        JSONObject another = (JSONObject)thing.get("3");
        System.out.println(another.get("1"));
        System.out.println(thing.get("3"));


    }
}
