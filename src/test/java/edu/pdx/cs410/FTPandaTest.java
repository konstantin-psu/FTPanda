package edu.pdx.cs410;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by kmacarenco on 7/13/15.
 */
public class FTPandaTest {
    static FTPServer testServer = new FTPServer();
    FTPanda ftpClient = new FTPanda();
    final String USER;
    static final String USERCONFIG = "ftpandaserver.config";
    final String PASS;
    static final String SERVER = "localhost";
    static final int PORT = 2221;
    final String USER_HOMEDIR;
    static List<File> fileList = new ArrayList<File>();

    private void setupUser(){
        String command = "user "+USER+" "+PASS;
        assertEquals(command, ftpClient.run(command), true);
    }
    private void connectToServer(){
        String command = "ftp "+SERVER+" "+PORT;
        assertEquals(command, ftpClient.run(command), true);
    }

    public static ArrayList<String> loadUserInfo(String filePath) {
        String line;
        ArrayList<String> uinfo = new ArrayList<String>();
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                uinfo.add(line);
            }
            bufferedReader.close();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return uinfo;
    }

    public FTPandaTest() {
        ArrayList<String> uinfo = loadUserInfo(USERCONFIG);
        USER = uinfo.get(0);
        PASS = uinfo.get(1);
        USER_HOMEDIR = uinfo.get(2);
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
        File tfile = new File(USER_HOMEDIR, fname);
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
        File tfile = new File(USER_HOMEDIR, fname);
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
        File tdir = new File(USER_HOMEDIR, dirname);
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
        File tdir = new File(USER_HOMEDIR, dirname);
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

   @Test
    public void RemoteCreateFolder() {
        //manually add subfolder to server folder, verify in output
        String dname = "test_dir1";

        setupUser();
        connectToServer();
        String command = "rmkdir " + dname;
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
        assertEquals("Checking output", outStream.toString().contains(dname), true);
    }

    @Test
    public void RemoteDeleteFolder() {
        //manually add subfolder to server folder, verify in output
        String dname = "test_dir1";

        setupUser();
        connectToServer();
        String command = "rrmdir " + dname;
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
        assertEquals("Checking output", outStream.toString().contains(dname), false);
    }

    @Test
    public void LocalListFileAndCD() {
        //manually add file to wd folder, do local list, verify in output
        String fname = "l_test_file.txt";
        String dname = "l_test_directory";
        File dfile = new File(FTPanda.cwd, dname);
        dfile.mkdir();
        File tfile = new File(dname, fname);
        try {
            tfile.createNewFile();
            fileList.add(tfile);
        }
        catch (IOException ex){
            fail("Failed to create the test file");
        }
        finally{
            fileList.add(dfile);
        }
        setupUser();
        connectToServer();
        String command = "lcd " + dname;
        Boolean status = ftpClient.run(command);
        assertEquals(command, true, status);
        command = "lls";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(outStream);
        PrintStream old = System.out;
        System.setOut(ps);
        status = ftpClient.run(command);
        System.out.flush();
        System.setOut(old);
        assertEquals(command, true, status);
        System.out.print(outStream.toString());
        assertEquals("Checking output", true, outStream.toString().contains(fname));
    }
}
