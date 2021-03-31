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
            Scanner sc = new Scanner(System.in);
            while (true) {
                String messageFromServer = in.readLine();
                System.out.println("Response: " + messageFromServer);

                String requestMessage = sc.nextLine();
                out.println(requestMessage);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
