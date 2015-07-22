package edu.pdx.cs410;

import org.apache.commons.net.ftp.FTP;

/**
 * Created by kmacarenco on 7/13/15.
 */
public class FTPandaRunner {
    public static void main(String[] args) {
        FTPanda ftpClient = new FTPanda(new Connection());
        ftpClient.mainLoop();
    }

}
