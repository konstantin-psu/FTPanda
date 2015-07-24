package edu.pdx.cs410;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Slick on 7/23/15.
 */
public class Client {
    private FTPClient ftp;
    private ConnectionInfo connectionInfo;

    public Client() {
        ftp = new FTPClient();
        connectionInfo = new ConnectionInfo();
    }

    public void connect() {
        int reply;
        boolean success;
        String user = connectionInfo.getUser();
        String password = connectionInfo.getPassword();
        int port = connectionInfo.getPort();
        String server = connectionInfo.getServer();

        try {
            if (ftp.isConnected()) {
                System.out.println(">> Already connected. Try disconnecting first.");
            } else {
                ftp.connect(server, port);
                showServerReply(ftp);
                System.out.println(">> Connected to " + server);
                System.out.println(">> " + ftp.getReplyString());

                //Check reply code to see if it was a success
                reply = ftp.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftp.disconnect();
                    System.out.println(">> FTP server refused to connect!");
                    return;
                }
                success = ftp.login(user, password);
                showServerReply(ftp);
                if (!success) {
                    System.out.println(">> Could not login to the server with " + user);
                } else {
                    System.out.println(">> Logged in successfully with " + user);
                }
                //Testing to see if it can create a directory as soon as it connects.
                //createDirectory("root");
            }
        } catch (SocketException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
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

    public void logout() {
        boolean success;

        try {
            success = ftp.logout();
            if (!success) {
                System.out.println("Could not logout of the server.");
            } else {
                System.out.printf("Logged out successfully");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void disconnect() {
        try {
            logout();
            ftp.disconnect();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void listFiles() {
        FTPFile[] files;
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        String details;

        try {
            files = ftp.listFiles();
            showServerReply(ftp);
            for (FTPFile file : files) {
                details = file.getName();
                if (file.isDirectory()) {
                    details = "[" + details + "}";
                }
                details += "\t\t" + file.getSize();
                details += "\t\t" + df.format(file.getTimestamp());
                System.out.println(details);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void createDirectory(String name) {
        boolean success;
        String dirName = name = "/" + name;
        try {
            success = ftp.makeDirectory(dirName);
            showServerReply(ftp);
            if (success) {
                System.out.println("Successfully created a directory: " + dirName);
            } else {
                System.out.println("Failed to create directory. See server's reply.");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }
}
