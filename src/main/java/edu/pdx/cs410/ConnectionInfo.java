package edu.pdx.cs410;

/**
 * @author konstantin
 * @version 1.1
 */
public class ConnectionInfo {
    private String server;
    private int port;
    private String user;
    private String password;

    public String getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
