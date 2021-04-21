package za.co.wethinkcode.robot.client;

import java.io.BufferedReader;
import java.io.IOException;

// Thread will listen for input from the server constantly.
public class ListenerThread implements Runnable {
    private final BufferedReader input;

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
                    String response = handler.convertJSON(messageFromServer);
                    String[] cutLastBit = messageFromServer.split("]");
                    String[] cutFirstBit = cutLastBit[0].split(":");
                    String position = cutFirstBit[cutFirstBit.length-1]+"]";
                    System.out.println("Response: "+response+" "+position);
//                    System.out.println("Response: "+messageFromServer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}