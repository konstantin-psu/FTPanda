package edu.pdx.cs410;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by konstantin on 7/11/15.
 */
public class Connection {
    public FTPClient ftpClient = new FTPClient();
    public ConnectionInfo cInfo = new ConnectionInfo();
    public boolean connected = false;
    public void connect() {
            String server = cInfo.server;
            int port = cInfo.port;
            String user = cInfo.user;
            String pass = cInfo.password;
            try {
                if (connected) {
                    System.out.println("Already connected, disconnect first.");
                } else {
                    ftpClient.connect(server, port);
                    showServerReply(ftpClient);
                    int replyCode = ftpClient.getReplyCode();
                    if (!FTPReply.isPositiveCompletion(replyCode)) {
                        System.out.println("Operation failed. Server reply code: " + replyCode);
                        return;
                    }
                    boolean success = ftpClient.login(user, pass);
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
            success = ftpClient.logout();
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
            ftpClient.disconnect();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void listFiles() {
        String currentDir = rpwd();
        rls(currentDir);
    }
    public void rls(String fileLocation) {
        FTPFile[] files;
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        String details;

        try {
            files = ftpClient.listFiles(fileLocation);
            showServerReply(ftpClient);
            for (FTPFile file : files) {
                details = file.getName();
                if (file.isDirectory()) {
                    details = "[" + details + "}";
                }
                details += "\t\t" + file.getSize();
//                details += "\t\t" + df.format(file.getTimestamp());
                System.out.println(details);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void createDirectory(String name) {
        boolean success;
        String dirName = name;
        try {
            success = ftpClient.makeDirectory(dirName);
            showServerReply(ftpClient);
            if (success) {
                System.out.println("Successfully created a directory: " + dirName);
            } else {
                System.out.println("Failed to create directory. See server's reply.");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    public String rpwd() {
        String pathName = null;
        try {
            pathName = ftpClient.printWorkingDirectory();
            showServerReply(ftpClient);
            if (pathName != null) {
                System.out.println(">>> " + pathName);
            } else {
                System.out.println("Failed to fetch current working directory. See server's reply.");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return pathName;
    }


    public String cd(String argument) {
        Boolean success;
        String returnMessage = null;
        try {
            success = ftpClient.changeWorkingDirectory(argument);
            showServerReply(ftpClient);
            if (success) {
                returnMessage = "current directory: "+argument;
                System.out.println(returnMessage);
            } else {
                System.out.println("Failed to change directory. See server's reply.");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return returnMessage;
    }

//    public ConnectionInfo getConnectionInfo() {
//        return connectionInfo;
//    }
}
