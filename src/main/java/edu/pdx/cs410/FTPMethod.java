package edu.pdx.cs410;

import org.omg.CORBA.DynAnyPackage.Invalid;

import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by konstantin on 7/11/15.
 **/

public class FTPMethod {
    public Integer numberOfArguments;
    public String description;
    public String name;
    public String usage;

    public FTPMethod(String nm, String use, String desc) {
        description = desc;
        usage = use;
        name = nm;
    }
    public void run(String [] commands) {
        System.out.println("Should not call it");
    }

    public String HelpString() {
        int descritptionPosition = 20;
        int offset = descritptionPosition - name.length();
        char [] spaces = new char[offset];
        Arrays.fill(spaces, ' ');
        String offsetPosition = new String(spaces);

        return name+" "+usage+"\n  "+description;
    }
}

class HelpCommand extends FTPMethod implements  commandInterface {
    Options optionsHost;
    public HelpCommand(String name, String use, String description, Options optionsHolder) {
        super(name, use, description);
        optionsHost = optionsHolder;
    }

    public void run(Command commands) {
        if (commands.length() == 1) {
            Iterator it = optionsHost.iterator();
            while(it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                System.out.println( ((commandInterface) pair.getValue()).HelpString());
            }
        } else {
            Iterator it = commands.iterator();
            while(it.hasNext()) {
                String name = (String) it.next();
                commandInterface commandInfo =  optionsHost.getCommandInfo(name);
                if (commandInfo == null) {
                    System.out.println("Unknown command "+name);
                } else {
                    System.out.println(commandInfo.HelpString());
                }
            }
        }

    }
}

class ExitCommand extends FTPMethod implements commandInterface {
    public ExitCommand(String name, String use, String description) {
        super(name, use, description);
    }

    public void run(Command commands) {
        System.out.println("Goodbye");
        System.exit(0);
    }
}

class UserCommand extends FTPMethod implements commandInterface {
    public UserCommand(String name, String use, String description) {
        super(name, use, description);
    }
    public void printUser(Command commands) {
        String user = commands.ftpConnection.cInfo.user;
        if (user.length() == 0) {
            System.out.println("No user set yet");
        } else {
            System.out.println(user);
        }
    }
    public void run(Command commands) throws InvalidCommand {
        String [] args = commands.arguments; //user [username] [password]
        if (args.length == 1)
        {
            printUser(commands);
            return;
        }

        if (args.length < 2) {
            throw new InvalidCommand (name + ' ' + usage);
        }
        commands.ftpConnection.cInfo.user = args[1];
        commands.ftpConnection.cInfo.password = "";

        if (args.length == 3) {
            commands.ftpConnection.cInfo.password = args[2];
        }


    }
}

class FtpCommand extends FTPMethod implements commandInterface {
    public FtpCommand(String name, String use, String description) {
        super(name, use, description);
    }

    public void run(Command commands) throws InvalidCommand, CommandFailed {
        String [] args = commands.arguments;
        if (args.length != 3) {
            throw new InvalidCommand (name + ' ' + usage);
        }
        commands.ftpConnection.cInfo.server = args[1];
        commands.ftpConnection.cInfo.port = new Integer(args[2]);
        commands.ftpConnection.connect();
    }
}

class LogoffCommand extends FTPMethod implements commandInterface {
    public LogoffCommand(String name, String use, String description) {
        super(name, use, description);
    }

    public void run(Command commands) throws InvalidCommand, CommandFailed {
        String [] args = commands.arguments; //user [username] [password]
        if (args.length != 1) {
            throw new InvalidCommand (name + ' ' + usage);
        }
        commands.ftpConnection.disconnect();
    }
}

class ListRemoteCommand extends FTPMethod implements commandInterface {
    public ListRemoteCommand(String name, String use, String description) {
        super(name, use, description);
    }

    public void run(Command commands) throws InvalidCommand, CommandFailed {
        if (commands.length() != 1) {
            throw new InvalidCommand(name + ' ' + usage);
        }
        commands.ftpConnection.listFiles();
    }
}

class MakeDirectoryRemoteCommand extends FTPMethod implements commandInterface {
    public MakeDirectoryRemoteCommand(String name, String use, String description) {
        super(name, use, description);
    }

    public void run(Command commands) throws InvalidCommand, CommandFailed {
        if (commands.length() != 2) {
            throw new InvalidCommand(name + ' ' + usage);
        }

        commands.ftpConnection.createDirectory(commands.arguments[1]);
    }
}

class DeleteFileRemoteCommand extends FTPMethod implements commandInterface {
    public DeleteFileRemoteCommand(String name, String use, String description) {
        super(name, use, description);
    }

    public void run(Command commands) throws InvalidCommand, CommandFailed {
        if (commands.length() != 2) {
            throw new InvalidCommand(name + ' ' + usage);
        }

        commands.ftpConnection.deleteFile(commands.arguments[1]);
    }
}

class DeleteDirectoryRemoteCommand extends FTPMethod implements commandInterface {
    public DeleteDirectoryRemoteCommand(String name, String use, String description) {
        super(name, use, description);
    }

    public void run(Command commands) throws InvalidCommand, CommandFailed {
        if (commands.length() != 2) {
            throw new InvalidCommand(name + ' ' + usage);
        }

        commands.ftpConnection.removeDirectory(commands.arguments[1]);
    }
}

class PWDRemoteCommand extends FTPMethod implements commandInterface {
    public PWDRemoteCommand(String name, String use, String description) {
        super(name, use, description);
    }

    public void run(Command commands) throws InvalidCommand, CommandFailed {
        if (commands.length() != 1) {
            throw new InvalidCommand(name + ' ' + usage);
        }

        commands.ftpConnection.rpwd();
    }
}

class CDRemoteCommand extends FTPMethod implements commandInterface {
    public CDRemoteCommand(String name, String use, String description) {
        super(name, use, description);
    }

    public void run(Command commands) throws InvalidCommand, CommandFailed {
        if (commands.length() != 2) {
            throw new InvalidCommand(name + ' ' + usage);
        }

        commands.ftpConnection.cd(commands.arguments[1]);
    }
}

class CDLocalCommand extends FTPMethod implements commandInterface {
    public CDLocalCommand(String name, String use, String description) {
        super(name, use, description);
    }

    public void run(Command commands) throws InvalidCommand, CommandFailed {
        if (commands.length() != 2) {
            throw new InvalidCommand(name + ' ' + usage);
        }

        commands.ftpConnection.lcd(commands.arguments[1]);
    }
}

class ListLocalCommand extends FTPMethod implements commandInterface {
    public ListLocalCommand(String name, String use, String description) {
        super(name, use, description);
    }

    public void run(Command commands) throws InvalidCommand {
        if (commands.length() != 1) {
            throw new InvalidCommand(name + ' ' + usage);
        }
        commands.ftpConnection.lls();
    }
}

class PutCommand extends FTPMethod implements commandInterface{
    public PutCommand(String name, String use, String description){
        super(name, use, description);
    }
    public void run(Command commands) throws InvalidCommand, CommandFailed{
        if (commands.length() != 3)
            throw new InvalidCommand(name + ' ' + usage);
        commands.ftpConnection.put(commands.arguments[1], commands.arguments[2]);
    }
}

class GetCommand extends FTPMethod implements commandInterface {
    public GetCommand(String name, String use, String description) {
        super(name, use, description);
    }

    public void run(Command commands) throws InvalidCommand, CommandFailed {
        int size = commands.length();

        if (size < 3) {
            throw new InvalidCommand(name + ' ' + usage);
        }
        else {
            for (int i = 1 ; i <= size-2; ++i) {
                commands.ftpConnection.get(commands.arguments[i], commands.arguments[size-1]);
            }

        }
    }
}
