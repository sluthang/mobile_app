package za.co.wethinkcode.robot.client;

import java.util.Arrays;
import java.util.List;

public class KindCreator {
    public static List<String> getKind(String kind){
        String shields = "999";
        String shots = "0";
        switch (kind.toLowerCase()) {
            case "sniper": shots = "1";
            case "trooper": shots = "5";
            default: shots = "3";
        }
        return Arrays.asList(kind, shields, shots);
    }
}
