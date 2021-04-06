package za.co.wethinkcode.robot.client;

import java.io.PrintStream;
import java.util.Scanner;

// Thread is used for sending output to server at any time during the programs execution.
public class OutputThread implements Runnable{
    PrintStream output;
    Scanner sc;

    public OutputThread(PrintStream out) {
        this.output = out;
        this.sc = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            String requestMessage = sc.nextLine();
            output.println(requestMessage);
            output.flush();
        }
    }
}
