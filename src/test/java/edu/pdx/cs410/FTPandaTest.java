package edu.pdx.cs410;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by kmacarenco on 7/13/15.
 */
public class FTPandaTest {
    static FTPServer testServer = new FTPServer();
    FTPanda ftpClient = new FTPanda();
    static final String USER = "myNewUser";
    static final String PASS = "secret";
    static final String SERVER = "localhost";
    static final int PORT = 2221;
    static final String SERVER_ROOT = "/root";
    static List<File> fileList = new ArrayList<File>();

    private void setupUser(){
        String command = "user "+USER+" "+PASS;
        assertEquals(command, ftpClient.run(command), true);
    }
    private void connectToServer(){
        String command = "ftp "+SERVER+" "+PORT;
        assertEquals(command, ftpClient.run(command), true);
    }

    @BeforeClass
    public static void setUp() {
        System.out.println("setting up");
        testServer.run();
    }

    @AfterClass
    public static void tearDown() {
        for (File f : fileList){
            f.delete();
        }
    }

    @Test
    public void getUserNameAndPass() {
        setupUser();
        assertEquals("User name should be", USER, ftpClient.ftpConnection.cInfo.user);
        assertEquals("User password should be", PASS, ftpClient.ftpConnection.cInfo.password);
    }

    @Test
    public void getServerAddressAndConnect() {
        setupUser();
        connectToServer();
        assertEquals("Server address should be", SERVER, ftpClient.ftpConnection.cInfo.server);
        assertEquals("Port should be", PORT, ftpClient.ftpConnection.cInfo.port);
        assertEquals("Connection status should be", true, ftpClient.ftpConnection.connected);
    }

    @Test
    public void ConnectLogoffConnect() {
        setupUser();
        connectToServer();
        String command = "logoff";
        assertEquals(command, ftpClient.run(command), true);
        assertEquals("Connection status should be", false, ftpClient.ftpConnection.connected);
        connectToServer();
    }

    @Test
    public void RemoteListFile() {
        //manually add file to server folder, do remote list, verify in output
        String fname = "test_file.txt";
        File tfile = new File(SERVER_ROOT, fname);
        try {
            tfile.createNewFile();
            fileList.add(tfile);
        }
        catch (IOException ex){
            fail("Failed to create the test file");
        }
        setupUser();
        connectToServer();
        String command = "rls";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(outStream);
        PrintStream old = System.out;
        System.setOut(ps);
        Boolean status = ftpClient.run(command);
        System.out.flush();
        System.setOut(old);
        assertEquals(command, status, true);
        assertEquals("Checking output", outStream.toString().contains(fname), true);
    }

    @Test
    public void RemoteDeleteFile() {
        //manually add file to server folder, do remote delete, verify in output
        String fname = "test_file.txt";
        File tfile = new File(SERVER_ROOT, fname);
        try {
            tfile.createNewFile();
            fileList.add(tfile);
        }
        catch (IOException ex){
            fail("Failed to create the test file");
        }
        setupUser();
        connectToServer();
        String command = "delete " + fname;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(outStream);
        PrintStream old = System.out;
        System.setOut(ps);
        Boolean status = ftpClient.run(command);
        System.out.flush();
        assertEquals(command, status, true);
        outStream.reset();

        status = ftpClient.run("rls");
        System.out.flush();
        System.setOut(old);
        assertEquals(command, status, true);
        assertEquals("Checking output", outStream.toString().contains(fname), false);
    }

    @Test
    public void RemoteListDirectory() {
        //manually add file to server folder, do remote list, verify in output
        String dirname = "test_directory";
        File tdir = new File(SERVER_ROOT, dirname);
        tdir.mkdir();
        fileList.add(tdir);
        setupUser();
        connectToServer();
        String command = "rls";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(outStream);
        PrintStream old = System.out;
        System.setOut(ps);
        Boolean status = ftpClient.run(command);
        System.out.flush();
        System.setOut(old);
        assertEquals(command, status, true);
        assertEquals("Checking output", outStream.toString().contains(dirname), true);
    }

    @Test
    public void RemoteListShouldNotSeeFileInDirectory() {
        //manually add file to server folder, do remote list, verify in output
        String dirname = "test_directory";
        String fname = "test_file_sub.txt";
        File tdir = new File(SERVER_ROOT, dirname);
        tdir.mkdir();
        fileList.add(tdir);
        File tfile = new File(tdir, fname);
        try {
            tfile.createNewFile();
            fileList.add(tfile);
        }
        catch (IOException ex){
            fail("Failed to create the test file");
        }
        setupUser();
        connectToServer();
        String command = "rls";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(outStream);
        PrintStream old = System.out;
        System.setOut(ps);
        Boolean status = ftpClient.run(command);
        System.out.flush();
        System.setOut(old);
        assertEquals(command, status, true);
        assertEquals("Checking file not seen", outStream.toString().contains(fname), false);
    }

}
