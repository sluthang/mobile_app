package za.co.wethinkcode.robot.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (
                Socket socket = new Socket("localhost", 6969);
                PrintStream out = new PrintStream(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()))
        )
        {
            Thread listener = new Thread(new ListenerThread(in));
            listener.start();

            Thread output = new Thread(new OutputThread(out));
            output.start();
            while (true) {}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
