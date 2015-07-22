package edu.pdx.cs410;

import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by kmacarenco on 7/13/15.
 */
public class FTPandaTest extends EasyMockSupport {
    private FTPanda ftpClient;

    private Connection mock;

    @Before
    public void setUp() {
        mock = createMock(Connection.class);
        ftpClient = new FTPanda(mock);
    }

    @Test
    public void getUserNameAndPass() {
        // Constants
        String user = "";
        String pass = "";
        String command = "user "+user+" "+pass;

        // Set up connection info for mock.
        ConnectionInfo cInfo = new ConnectionInfo();
        cInfo.setUser(user);
        cInfo.setPassword(pass);

        // Set up mock.
        expect(mock.getConnectionInfo()).andReturn(cInfo).anyTimes(); // Run twice by connect.
        replay(mock);

        // Test class.
        ftpClient.run(command);

        // Verify the mock did what it was supposed to.
        verify(mock);

        // Assert values.
        assertEquals("User name should be", user, ftpClient.ftpConnection.getConnectionInfo().getUser());
        assertEquals("User password should be", pass, ftpClient.ftpConnection.getConnectionInfo().getPassword());
    }

    @Test
    public void getServerAddressAndConnect() {
        // Constants
        String server = "localhost";
        int port = 21;
        String command = "ftp localhost 21";

        // Set up connection info for mock.
        ConnectionInfo cInfo = new ConnectionInfo();
        cInfo.setServer(server);
        cInfo.setPort(port);

        // Set up mock.
        expect(mock.getConnectionInfo()).andReturn(cInfo).anyTimes(); // Run twice by connect.
        mock.connect();
        replay(mock);

        // Test class.
        ftpClient.run(command);

        // Verify the mock.
        verify(mock);

        // Assert connection values.
        assertEquals("Server address should be", server, ftpClient.ftpConnection.getConnectionInfo().getServer());
        assertEquals("Port should be", port, ftpClient.ftpConnection.getConnectionInfo().getPort());

        // Commented out because this requires more extensive mocking to fake correctly.
        //assertEquals("Connection status should be", true, ftpClient.ftpConnection.connected);
    }

    @Test
    public void exitClient() {}


}