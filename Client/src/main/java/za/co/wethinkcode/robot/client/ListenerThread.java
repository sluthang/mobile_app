package za.co.wethinkcode.robot.client;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;

// Thread will listen for input from the server constantly.
public class ListenerThread implements Runnable {
    private final BufferedReader input;
    public static String type;

    public ListenerThread(BufferedReader in) {
        input = in;
    }

    public void run() {
        String messageFromServer = "";
        StringHandler handler = new StringHandler();

        while (messageFromServer != null) {
            try {
                messageFromServer = input.readLine();
                if (messageFromServer != null){
                    PrettyPrint print = new PrettyPrint(messageFromServer);
                    print.printMessage();
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }
}