package za.co.wethinkcode.robot.client;

import java.io.PrintStream;
import java.util.Scanner;

// Thread is used for sending output to server at any time during the programs execution.
public class OutputThread implements Runnable{
    PrintStream output;
    Scanner sc;
    static String name;

    public OutputThread(PrintStream out) {
        this.output = out;
        this.sc = new Scanner(System.in);
    }

    public void run() {
        boolean launched = false;

        while (true) {
            System.out.println("What should I do next?");
            String requestMessage = sc.nextLine();
            try {
                requestMessage = JsonHandler.convertCommand(requestMessage, name);
            } catch (IllegalArgumentException e) {
                System.out.println("Something bonk");
                continue;
            }
            if (launched || JsonHandler.isLaunch(requestMessage)) {
                output.println(requestMessage);
                output.flush();
                launched = true;
            }
            else {
                System.out.println("Please launch a robot first.");
            }
        }
    }
}
