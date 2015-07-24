package edu.pdx.cs410;

import org.omg.CORBA.DynAnyPackage.Invalid;

import javax.swing.text.html.Option;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by konstantin on 7/11/15.
 **/

class HelpOption extends OptionInterface {
    Options optionsHost;

    //Instantiate new helpCommand object
    public HelpOption(String option, String description, Options optionsHolder) {
        super(option, description);
        optionsHost = optionsHolder;
    }

    public void run(String[] input, Client client) {

        //If 'help' was the only input from the user
        if (input.length == 1) {
            Iterator it = optionsHost.iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                System.out.println(((OptionInterface) pair.getValue()).helpString());
            }
        }
        //If they want help with a specific command
        else {
            OptionInterface command;

            for (String each : input) {
                command = optionsHost.getCommandInfo(each);
                if (each.equals("help")) {
                    command = null;
                }
                if (command == null) {
                    System.out.println("Unknown command " + each);
                } else {
                    System.out.println(command.helpString());
                }
            }
        }
    }

}

class ExitOption extends OptionInterface {
    public ExitOption(String option, String description) {
        super(option, description);
    }

    public void run(String[] input, Client client) {
        System.out.println(">> Disconnecting from the server...");
        client.disconnect();
        System.exit(0);
    }
}

class UserOption extends OptionInterface {
    public UserOption(String option, String description) {
        super(option, description);
    }

    public void printUser(String[] input, Client client) {
        String user = client.getConnectionInfo().getUser();
        if (user.length() == 0) {
            System.out.println("There is no user set yet.");
        } else {
            System.out.println("User: " + user);
        }
    }

    public void run(String[] input, Client client) throws InvalidCommand {

        int length = input.length;

        if (length == 1) {
            printUser(input, client);
            return;
        } else if (length == 3) {
            client.getConnectionInfo().setUser(input[1]);
            client.getConnectionInfo().setPassword((input[2]));
        } else {
            throw new InvalidCommand("User [username] [password]");
        }

    }
}

class FtpOption extends OptionInterface {
    public FtpOption(String option, String description) {
        super(option, description);
    }

    public void run(String[] input, Client client) throws InvalidCommand {
        int length = input.length;

        if (length != 3) {
            throw new InvalidCommand("Incorrect Format. Try: ftp [ftp_server_address] [port]");
        } else if (client.getConnectionInfo().getUser().equals("") &&
                client.getConnectionInfo().getPassword().equals("")) {
            throw new InvalidCommand("Enter in a username and password first before connecting!");
        }

        client.getConnectionInfo().setServer(input[1]);
        client.getConnectionInfo().setPort(Integer.parseInt(input[2]));
        client.connect();
    }
}

class LogoffOption extends OptionInterface {
    public LogoffOption(String option, String description) {
        super(option, description);
    }

    public void run(String[] input, Client client) throws InvalidCommand {
        int length = input.length;

        if (length != 1) {
            throw new InvalidCommand(option);
        }
        client.disconnect();
    }
}

class ListOption extends OptionInterface {
    public ListOption(String option, String description) {
        super(option, description);
    }

    public void run(String[] input, Client client) throws InvalidCommand {
        if (input.length != 1) {
            throw new InvalidCommand(option);
        }

        client.listFiles();
    }
}

class MakeDirectoryOption extends OptionInterface {
    public MakeDirectoryOption(String option, String description) {
        super(option, description);
    }

    public void run(String[] input, Client client) throws InvalidCommand {
        if (input.length != 2) {
            throw new InvalidCommand(option);
        }

        client.createDirectory(input[1]);
    }
}
