package edu.pdx.cs410;

import java.util.Scanner;

/**
 * Created by Harley on 7/9/2015.
 */
public class FTPanda {
    public Connection ftpConnection;
    private Options opt = new Options();
    private String delimeter = null;

    public FTPanda (Connection connection) {
        ftpConnection = connection;
        delimeter = ">>>";
    }
    private void printlnCLI(String toPrint) {
        printCLI(toPrint+"\n");
    }

    private void printCLI() {
        System.out.print(delimeter + " ");
    }
    private void printCLI(String toPrint) {
        System.out.print(delimeter + " " + toPrint);
    }

    public void mainLoop() {
        printlnCLI("Welcome to FTPanda");
        while(true) {
            Scanner scanner = new Scanner(System.in);
            printCLI();
            String currentLine = scanner.nextLine();
            run(currentLine);

        }
    }

    public void run(String command) {
        Command currentCommand = new Command(toArray(command), ftpConnection);
        opt.action(currentCommand);
    }

    private String [] toArray(String input) {
        if (input.contains(" "))  {
            return input.split(" ");
        }
        String [] ret = new String[1];
        ret[0] = input;
        return ret;
    }
}

