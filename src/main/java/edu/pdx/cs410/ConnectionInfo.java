package edu.pdx.cs410;

/**
 * Created by konstantin on 7/11/15.
 */
public class ConnectionInfo {
    public String server;
    int port;
    String user;
    String password;

    public ConnectionInfo() {
        server = "localhost";
        port = 2221;
        user = "";
        password = "";
    }
}
