package za.co.wethinkcode.robot.client;

import org.json.simple.JSONObject;

import java.io.PrintStream;
import java.util.Scanner;

// Thread is used for sending output to server at any time during the programs execution.
public class OutputThread implements Runnable{
    PrintStream output;
    Scanner sc;
    String name;

    public OutputThread(PrintStream out) {
        this.output = out;
        this.sc = new Scanner(System.in);
    }

    public void run() {
        name = getName();
        boolean launched = false;

        while (true) {
            System.out.println("What should I do next?");
            String requestMessage = sc.nextLine();

            requestMessage = JsonHandler.convertCommand(requestMessage, name);

            if (launched || JsonHandler.isLaunch(requestMessage)) {
                output.println(requestMessage);
                output.flush();
            }
            else {
                System.out.println("Please launch a robot first.");
            }
        }
    }

    private String getName() {
        System.out.println("What is your robot's name?");
        return sc.nextLine();
    }
}
