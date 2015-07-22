package edu.pdx.cs410;

import org.apache.commons.net.ftp.FTP;
import org.apache.ftpserver.*;

/**
 * Created by kmacarenco on 7/13/15.
 */
public class FTPandaRunner {
    public static void main(String[] args) {
        try {
            FtpServerFactory serverFactory = new FtpServerFactory();
            FtpServer server = serverFactory.createServer();
            // start the server
            server.start();

            FTPanda ftpClient = new FTPanda();
            ftpClient.mainLoop();
        } catch (Exception e) {
              System.out.println(e.toString());
        }
    }

    public void setUser() {
//        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
//        userManagerFactory.setFile(new File(&quot;myusers.properties&quot;));
//        userManagerFactory.setPasswordEncryptor(new SaltedPasswordEncryptor());
//        UserManager um = userManagerFactory.createUserManager();
//        BaseUser user = new BaseUser();
//        user.setName(&quot;myNewUser&quot;);
//        user.setPassword(&quot;secret&quot;);
//        user.setHomeDirectory(&quot;ftproot&quot;);
//        um.save(user);
    }

}
