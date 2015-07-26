package edu.pdx.cs410;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by konstantin on 7/12/15.
 */
public class Command {
    String [] arguments;
    String name;
    Connection ftpConnection;

    public Command(String [] args, Connection ftp) {
        arguments = args;
        ftpConnection = ftp;
        name = arguments[0];
    }
    public int length() {
        return arguments.length;
    }
    public Iterator iterator() {
        return ((Iterable<String>) Arrays.asList(arguments)).iterator();
    }
}

