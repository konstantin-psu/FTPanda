package edu.pdx.cs410;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kmacarenco on 7/13/15.
 */
public class FTPandaTest {
    FTPanda ftpClient = new FTPanda();

    @Test
    public void getUserNameAndPass() {
        String user = "";
        String pass = "";
        String command = "user "+user+" "+pass;
        ftpClient.run(command);
        assertEquals("User name should be", user, ftpClient.ftpConnection.cInfo.user);
        assertEquals("User password should be", pass, ftpClient.ftpConnection.cInfo.password);
    }

    @Test
    public void getServerAddressAndConnect() {
        String user = "";
        String pass = "";
        String ucommand = "user "+user+" "+pass;
        ftpClient.run(ucommand);
        String server = "localhost";
        int port = 21;
        String command = "ftp localhost 21";
        ftpClient.run(command);
        assertEquals("Server address should be", server, ftpClient.ftpConnection.cInfo.server);
        assertEquals("Port should be", port, ftpClient.ftpConnection.cInfo.port);
        assertEquals("Connection status should be", true, ftpClient.ftpConnection.connected);
    }

    @Test
    public void exitClient() {}


}