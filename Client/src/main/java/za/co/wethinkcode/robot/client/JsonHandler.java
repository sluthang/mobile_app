package za.co.wethinkcode.robot.client;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.util.Arrays;
import java.util.Collections;

public class JsonHandler {

    static public String convertCommand(String instruction, String name) {
        JSONObject newInstruction = new JSONObject();
        String[] instructions = instruction.split(" ");
        String command = instructions[0];

        switch (command) {
            case "left":
            case "right":
                newInstruction.put("arguments", Arrays.asList(command));
                command = "turn";
                break;
            case "mine":
            case "repair":
                newInstruction.put("arguments", Collections.emptyList());
                break;
            case "back" :
            case "forward" : {
                if (instructions.length > 1) {
                    newInstruction.put("arguments", Arrays.asList(instructions[1]));
                } else {
                    throw new IllegalArgumentException();
                }
                break;
            }
            case "launch":
                if (instructions.length == 3) {
                    OutputThread.name = instructions[2];
                    newInstruction.put("arguments", KindCreator.getKind(instructions[1]));
                }
                else {
                    throw new IllegalArgumentException();
                }
                break;
            default: throw new IllegalArgumentException();
            }

        newInstruction.put("robot", OutputThread.name);
        newInstruction.put("command", command.toLowerCase());

        return newInstruction.toJSONString();
    }

    public static boolean isLaunch(String jsonObject) {
        JSONObject jsonMessage = (JSONObject) JSONValue.parse(jsonObject);
        return (jsonMessage.get("command").equals("launch"));
    }
}