package za.co.wethinkcode.robot.client;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (
                Socket socket = new Socket("localhost", 5000);
                PrintStream out = new PrintStream(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()))
        )
        {
            Scanner sc = new Scanner(System.in);
            while (true) {
                String requestMessage = sc.nextLine();
                out.println(requestMessage);
                out.flush();
                String messageFromServer = in.readLine();
                if(messageFromServer.equals("off")) System.exit(0);
                System.out.println("Response: " + messageFromServer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
