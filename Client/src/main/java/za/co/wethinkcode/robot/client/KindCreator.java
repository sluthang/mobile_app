package za.co.wethinkcode.robot.client;

import java.util.Arrays;
import java.util.List;

public class KindCreator {
    public static List<String> getKind(String kind){
        String shields = "999";
        String shots;
        switch (kind.toLowerCase()) {
            case "sniper": shots = "1"; break;
            case "trooper": shots = "5"; break;
            case "miner": shots = "0"; break;
            default: shots = "3";
        }
        return Arrays.asList(kind, shields, shots);
    }
}
