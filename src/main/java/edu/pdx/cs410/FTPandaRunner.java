package edu.pdx.cs410;


/**
 * Created by kmacarenco on 7/13/15.
 */
public class FTPandaRunner {
    public static void main(String[] args) {
        FTPServer testServer = new FTPServer();
        testServer.run();
        FTPanda ftpClient = new FTPanda();
        ftpClient.run();
    }

}
