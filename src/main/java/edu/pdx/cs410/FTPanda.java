package edu.pdx.cs410;

import java.util.Scanner;

/**
 * Created by Slick on 7/23/15.
 */
public class FTPanda {
    private Client client;
    private Options option;

    public FTPanda() {
        client = new Client();
        option = new Options();
    }

    public void run() {
        String currentLine;
        System.out.println(">> Welcome to FTPanda!");
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print(">> ");
            currentLine = scanner.nextLine();
            parse(currentLine);
        }
    }

    public void parse(String input) {
        String[] userInput;

        if (input.equals("")) {
            return;
        }
        if (input.contains(" ")) {
            userInput = input.split(" ");
        } else {
            userInput = new String[]{input};
        }

        option.action(userInput, client);
    }
}
