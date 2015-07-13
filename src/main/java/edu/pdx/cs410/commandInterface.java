package edu.pdx.cs410;

/**
 * Created by konstantin on 7/11/15.
 */
public interface commandInterface {
    public void run(Command command) throws InvalidCommand;
    public String HelpString();
}
