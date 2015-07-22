package edu.pdx.cs410;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by kmacarenco on 7/13/15.
 */
public class FTPandaTest {
    static FTPServer testServer = new FTPServer();
    FTPanda ftpClient = new FTPanda();

    @BeforeClass
    public static void setUp() {
        System.out.println("setting up");
        testServer.run();
    }

    @Test
    public void getUserNameAndPass() {
        String user = "myNewUser";
        String pass = "secret";
        String command = "user "+user+" "+pass;
        ftpClient.run(command);
        assertEquals("User name should be", user, ftpClient.ftpConnection.cInfo.user);
        assertEquals("User password should be", pass, ftpClient.ftpConnection.cInfo.password);
    }

    @Test
    public void getServerAddressAndConnect() {
        String user = "myNewUser";
        String pass = "secret";
        String ucommand = "user "+user+" "+pass;
        ftpClient.run(ucommand);
        String server = "localhost";
        int port = 2221;
        String command = "ftp localhost 2221";
        ftpClient.run(command);
        assertEquals("Server address should be", server, ftpClient.ftpConnection.cInfo.server);
        assertEquals("Port should be", port, ftpClient.ftpConnection.cInfo.port);
        assertEquals("Connection status should be", true, ftpClient.ftpConnection.connected);
    }

    @Test
    public void exitClient() {}


}