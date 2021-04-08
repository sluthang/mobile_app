package za.co.wethinkcode.robot.client;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import java.lang.reflect.Array;
import java.util.Arrays;

public class JsonHandler {
    static public String convertCommand(String instruction, String name) {
        JSONObject newInstruction = new JSONObject();
        String[] instructions = instruction.split(" ");

        String[] arguments = new String[1];
        String command = "";

        if (instructions[0].toLowerCase().matches("^(left|right)$")) {
            command = "turn";
            arguments[0] = instructions[0].toLowerCase();
        }
        else {
            command  = instructions[0];
            arguments = Arrays.copyOfRange(instructions, 1, instructions.length);
        }

        newInstruction.put("robot", name);
        newInstruction.put("command", command);
        newInstruction.put("arguments", Arrays.asList(arguments));

        System.out.println(newInstruction.toJSONString());

        return null;
    }
}
