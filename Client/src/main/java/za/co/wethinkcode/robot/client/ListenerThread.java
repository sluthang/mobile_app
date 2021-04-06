package za.co.wethinkcode.robot.client;

import java.io.BufferedReader;
import java.io.IOException;

// Thread will listen for input from the server constantly.
public class ListenerThread implements Runnable {
    private BufferedReader input;

    public ListenerThread(BufferedReader in) {
        input = in;
    }

    public void run() {
        String messageFromServer = null;
        while (true) {
            try {
                messageFromServer = input.readLine();
                System.out.println("Response: " + messageFromServer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
