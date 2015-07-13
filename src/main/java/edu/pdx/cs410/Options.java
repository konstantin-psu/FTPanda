package edu.pdx.cs410;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by kmacarenco on 7/13/15.
 */

public class Options {
    Map<String, commandInterface> options = new HashMap<String, commandInterface>();


    //Add new functionality here.
    //All commands definitions live in FTPMethod.java
    public Options() {
        String name;

        name = "h";
        options.put(name, new HelpCommand(name, 0, "Print help for this application", this));

        name = "exit";
        options.put(name, new ExitCommand(name, 0, "Quit the application"));

        name = "user";
        options.put(name, new UserCommand(name, 3, "Format: user [uname] [passwd]"));

        name = "ftp";
        options.put(name, new FtpCommand (name, 2, "Format: ftp [server_address] [port]"));

        name = "logoff";
        options.put(name, new LogoffCommand (name, 2, "Close ftp connection"));
    }



    public void action(Command command) {
        try {
            if (options.containsKey(command.name)) {
                commandInterface currentArg = options.get(command.name);
                currentArg.run(command);
            } else {
                System.out.println("No such command.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public commandInterface getCommandInfo(String name) {
        return options.get(name);
    }

    public Iterator iterator() {
        return options.entrySet().iterator();
    }

}

class NotImplemented extends Exception {
    public NotImplemented(String exc) {
        super("Argument "+exc + " Not yet Implemented");

    }
}

class InvalidCommand extends Exception {
    public InvalidCommand(String exc) {
        super("Invalid command: "+exc);
    }
}


