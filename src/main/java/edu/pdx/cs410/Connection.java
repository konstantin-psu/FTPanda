package edu.pdx.cs410;

import org.apache.commons.net.ftp.FTPClient;
import java.io.IOException;
import org.apache.commons.net.ftp.FTPReply;

/**
 * Created by konstantin on 7/11/15.
 */
public class Connection {
    public FTPClient ftpClient = new FTPClient();
    public boolean connected = false;

    private ConnectionInfo cInfo = new ConnectionInfo();

    public void connect() {
            try {
                if (connected) {
                    System.out.println("Already connected, disconnect first.");
                } else {
                    ftpClient.connect(cInfo.getServer(), cInfo.getPort());
                    showServerReply(ftpClient);
                    int replyCode = ftpClient.getReplyCode();
                    if (!FTPReply.isPositiveCompletion(replyCode)) {
                        System.out.println("Operation failed. Server reply code: " + replyCode);
                        return;
                    }
                    boolean success = ftpClient.login(cInfo.getUser(), cInfo.getPassword());
                    showServerReply(ftpClient);
                    if (!success) {
                        System.out.println("Could not login to the server");
                        return;
                    } else {
                        System.out.println("LOGGED IN SERVER");
                        connected = true;
                    }
                }
            } catch (IOException ex) {
                System.out.println("Oops! Something wrong happened");
                ex.printStackTrace();

            }
    }

    public void disconnect() {
        try {
            ftpClient.disconnect();
            showServerReply(ftpClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

    public ConnectionInfo getConnectionInfo() {
        return cInfo;
    }
}
