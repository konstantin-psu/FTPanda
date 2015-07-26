package edu.pdx.cs410;

/**
 * Created by konstantin on 7/21/15.
 */
import org.apache.ftpserver.*;

import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.SaltedPasswordEncryptor;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FTPServer {
    private static Logger _logger;
    public void run() {
        try {
            FtpServerFactory serverFactory = new FtpServerFactory();
            ConnectionConfigFactory connectionConfigFactory = new ConnectionConfigFactory();
            connectionConfigFactory.setAnonymousLoginEnabled(true);
            serverFactory.setConnectionConfig(connectionConfigFactory.createConnectionConfig());

            setUser(serverFactory);
            ListenerFactory factory = new ListenerFactory();
            factory.setPort(2221);
            serverFactory.addListener("default", factory.createListener());

            FtpServer server = serverFactory.createServer();
            _logger = LoggerFactory.getLogger(FtpServer.class);
            // start the server
            server.start();
            TimeUnit.SECONDS.sleep(3);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    public static void setUser(FtpServerFactory serverFactory) {
        try {

            PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();

            File f = new File("myusers.properties");
            f.createNewFile();
            userManagerFactory.setFile(f);
            userManagerFactory.setPasswordEncryptor(new SaltedPasswordEncryptor());
            UserManager um = userManagerFactory.createUserManager();
            BaseUser user = new BaseUser();
            user.setName("myNewUser");
            user.setPassword("secret");
            user.setEnabled(true);
            user.setHomeDirectory("/root");
            List<Authority> authorities = new ArrayList<Authority>();
            authorities.add(new WritePermission());
            user.setAuthorities(authorities);
            um.save(user);
            serverFactory.setUserManager(um);

            BaseUser anon = new BaseUser();
            anon.setName("anonymous");
            serverFactory.getUserManager().save(anon);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch( keyCode ) {
            case KeyEvent.VK_UP:
                System.out.println("Key up");
                // handle up
                break;
            case KeyEvent.VK_DOWN:
                // handle down
                break;
            case KeyEvent.VK_LEFT:
                // handle left
                break;
            case KeyEvent.VK_RIGHT :
                // handle right
                break;
        }
    }

}
