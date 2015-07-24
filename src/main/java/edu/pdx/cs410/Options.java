package edu.pdx.cs410;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by kmacarenco on 7/13/15.
 * <p>
 * This class manages all the command line options available to the user. It stores commands such as ls, mkdir, exit,
 * login, ftp. Everything in here is user given input that will be parsed and then ran with proper action.
 */
public class Options {
    private Map<String, OptionInterface> options;


    //Add new functionality here.
    //All commands definitions live in optionAction.java

    public Options() {
        options = new HashMap<String, OptionInterface>();
        String name;

        name = "help";
        options.put(name, new HelpOption(name, "Print help for this application", this));

        name = "exit";
        options.put(name, new ExitOption(name, "Quit the application"));

        name = "user";
        options.put(name, new UserOption(name, "Format: user [username] [password]"));

        name = "ftp";
        options.put(name, new FtpOption(name, "Format: ftp [server_address] [port]"));

        name = "logoff";
        options.put(name, new LogoffOption(name, "Close ftp connection"));

        name = "ls";
        options.put(name, new ListOption(name, "Lists the files and folders in the directory."));

        name = "mkdir";
        options.put(name, new MakeDirectoryOption(name, "Makes a directory in your current path."));
    }

    public void action(String[] input, Client client) {
        try {
            String option = input[0];


            //Checks if it's a valid userInput
            if (options.containsKey(option)) {
                //Gets the OptionAction object for the corresponding option so it can execute
                OptionInterface currentArg = options.get(option);
                currentArg.run(input, client);
            }
            //If it's not a valid userInput
            else {
                System.out.println("That command does not exist!");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public OptionInterface getCommandInfo(String name) {
        return options.get(name);
    }

    //Returns the options as an iterator
    public Iterator iterator() {
        return options.entrySet().iterator();
    }

}

class NotImplemented extends Exception {
    public NotImplemented(String exc) {
        super("Argument " + exc + " Not yet Implemented");

    }
}

class InvalidCommand extends Exception {
    public InvalidCommand(String exc) {
        super("Invalid command: " + exc);
    }
}


