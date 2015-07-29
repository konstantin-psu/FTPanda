package edu.pdx.cs410;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by konstantin on 7/11/15.
 */
public class Connection {
    public FTPClient ftpClient = new FTPClient();
    public ConnectionInfo cInfo = new ConnectionInfo();
    public boolean connected = false;
    public void connect() throws CommandFailed {
        String server = cInfo.server;
        int port = cInfo.port;
        String user = cInfo.user;
        String pass = cInfo.password;
        if (connected) {
            throw new CommandFailed("Already connected, disconnect first.");
        } else {
            try {
                ftpClient.connect(server, port);
                showServerReply(ftpClient);
                int replyCode = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(replyCode)) {
                    throw new CommandFailed("Operation failed. Server reply code: " + replyCode);
                }
                boolean success = ftpClient.login(user, pass);
                showServerReply(ftpClient);
                if (!success) {
                    throw new CommandFailed("Could not login to the server");
                } else {
                    System.out.println("LOGGED IN SERVER");
                    connected = true;
                }
            }
            catch (IOException ex){
                throw new CommandFailed(ex.getMessage());
            }
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

    public void logout() throws CommandFailed{
        boolean success;
        try {
            success = ftpClient.logout();
            if (!success) {
                throw new CommandFailed("Could not logout of the server.");
            } else {
                System.out.println("Logged out successfully");
            }
        } catch (IOException e) {
            throw new CommandFailed(e.getMessage());
        }
    }

    public void disconnect() throws CommandFailed{
        if (!connected){
            throw new CommandFailed("Not connected to server");
        }
        try {
            logout();
            ftpClient.disconnect();
            connected = false;
        } catch (IOException e) {
            throw new CommandFailed(e.getMessage());
        }
    }

    public void listFiles() throws CommandFailed{
        String currentDir = rpwd();
        rls(currentDir);
    }
    public void rls(String fileLocation) throws CommandFailed{
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
            throw new CommandFailed(e.getMessage());
        }
    }

    public void createDirectory(String name) throws CommandFailed{
        boolean success;
        String dirName = name;
        try {
            success = ftpClient.makeDirectory(dirName);
            showServerReply(ftpClient);
            if (success) {
                System.out.println("Successfully created a directory: " + dirName);
            } else {
                throw new CommandFailed("Failed to create directory. See server's reply.");
            }
        } catch (IOException e) {
            throw new CommandFailed(e.getMessage());
        }

    }

    public void deleteFile(String name) throws CommandFailed {
        boolean success;
        try {
            success = ftpClient.deleteFile(name);
            showServerReply(ftpClient);
            if (success) {
                System.out.println("Successfully deleted file: " + name);
            } else {
                throw new CommandFailed("Failed to delete file. See server's reply.");
            }
        } catch (IOException e) {
            throw new CommandFailed(e.getMessage());
        }
    }

    public String rpwd() throws CommandFailed{
        String pathName = null;
        try {
            pathName = ftpClient.printWorkingDirectory();
            showServerReply(ftpClient);
            if (pathName != null) {
                System.out.println(">>> " + pathName);
            } else {
                throw new CommandFailed("Failed to fetch current working directory. See server's reply.");
            }
        } catch (IOException e) {
            throw new CommandFailed(e.getMessage());
        }
        return pathName;
    }


    public String cd(String argument) throws CommandFailed{
        Boolean success;
        String returnMessage = null;
        try {
            success = ftpClient.changeWorkingDirectory(argument);
            showServerReply(ftpClient);
            if (success) {
                returnMessage = "current directory: "+argument;
                System.out.println(returnMessage);
            } else {
                throw new CommandFailed("Failed to change directory. See server's reply.");
            }
        } catch (IOException e) {
            throw new CommandFailed(e.getMessage());
        }
        return returnMessage;
    }

    public void lcd(String argument) throws CommandFailed{
        File oldcwd = new File(FTPanda.cwd);
        File newcwd = new File(oldcwd, argument);
        try {
            String tmp = newcwd.getCanonicalPath();
            if (!newcwd.exists() || !newcwd.isDirectory()){
                throw new CommandFailed(tmp + " does not exist!!");
            }
            else {
                FTPanda.cwd = tmp;
            }
        }
        catch (IOException ex) {
            throw new CommandFailed(ex.getMessage());
        }
    }

    public void lls(){
        File cwd = new File(FTPanda.cwd);
        File[] files = cwd.listFiles();
        System.out.println(FTPanda.cwd + ':');
        for (File f : files){
            if (f.isDirectory())
                System.out.println(f.getName() + '/');
            else
                System.out.println(f.getName());
        }
    }

    public void put(String filename, String remotePath) throws CommandFailed{
        File cwd = new File(FTPanda.cwd);
        File f = new File(cwd, filename);
        InputStream fs;
        try {
            fs = new FileInputStream(f.getAbsolutePath());
        }
        catch (FileNotFoundException ex){
            throw new CommandFailed(filename + " not found!!");
        }
        try{
            Boolean success = ftpClient.storeFile(remotePath, fs);
            showServerReply(ftpClient);
            if (!success){
                throw new CommandFailed("Failed to upload " + filename + " to " + remotePath);
            }
            System.out.println("Successfully uploaded " + filename + " to " + remotePath);
        }
        catch (IOException ex){
            throw new CommandFailed(ex.getMessage());
        }
    }

    public void get(String filename, String localPath) throws CommandFailed {
        String remoteFile = rpwd() + "\\" +filename; //Gets the name of the file with the full path for the rmeote file
        String path = localPath.equals(".")? FTPanda.cwd : localPath;
        File downloadFile = new File(path + "\\" +filename); //Sets the filename for the new local file
        OutputStream outputStream = null;
        boolean success = false;
        try {
            //Writes remote file to the local directory file location
            outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
            success = ftpClient.retrieveFile(remoteFile, outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            throw new CommandFailed(e.getMessage());
        } catch (IOException e) {
            throw new CommandFailed(e.getMessage());
        }

        if (success) {
            System.out.println("File " + filename + " has been downloaded successfully.");
        }

    }

    public void removeDirectory(String name) throws CommandFailed {
        boolean success;
        try {
            success = ftpClient.removeDirectory(name);
            showServerReply(ftpClient);
            if (success) {
                System.out.println("Successfully removed directory: " + name);
            } else {
                throw new CommandFailed("Failed to remove directory. See server's reply.");
            }
        } catch (IOException e) {
            throw new CommandFailed(e.getMessage());
        }
    }
}
