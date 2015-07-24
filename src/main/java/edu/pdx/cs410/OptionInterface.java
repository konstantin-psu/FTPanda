package edu.pdx.cs410;

/**
 * Created by konstantin on 7/11/15.
 */
public abstract class OptionInterface {
    protected String option;
    protected String description;

    public OptionInterface(String option, String description) {
        this.option = option;
        this.description = description;
    }

    public abstract void run(String[] userInput, Client client) throws InvalidCommand;

    public String helpString() {
        return option + " - " + description;
    }
}
