package edu.pdx.cs410;

/**
 * Created by konstantin on 7/11/15.
 */
public class ConnectionInfo {
    private String server;
    private int port;
    private String user;
    private String password;

    public ConnectionInfo() {
        server = "localhost";
        port = 2221;
        user = "";
        password = "";
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
