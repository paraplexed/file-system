package test.FileSystem;

import main.FileSystem.Drive;
import main.FileSystem.FileSystem;
import main.FileSystem.FileSystemException;
import main.FileSystem.Folder;
import main.FileSystem.TextFile;
import main.FileSystem.ZipFile;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

/**
 * Created by dclifford on 11/15/16.
 */
public class MainTest {
    @Test
    public void testSize() throws FileSystemException {
        FileSystem fs = new FileSystem();
        Drive drive = fs.createDrive("Drive123");
        Folder folder = fs.createFolder("Drive123\\folder456");
        ZipFile zipFile = fs.createZipFile("Drive123\\zip789");
        TextFile textFile = null;
        for (int i = 0; i < 10; i++) {
            fs.createTextFile("Drive123\\folder456\\text456" + i, "Some text in a file in a folder");
            textFile = fs.createTextFile("Drive123\\zip789\\text456" + i, "Some text in a folder");
        }
        Assert.assertEquals("Drive size was incorrect", 520, drive.size());
        Assert.assertEquals("Folder size was incorrect", 310, folder.size());
        Assert.assertEquals("ZipFile size was incorrect", 210, zipFile.size());
        Assert.assertEquals("TextFile size was incorrect", 21, textFile.size());
    }

    @Test
    public void exceptionTests() throws FileSystemException {
        FileSystem fs = new FileSystem();
        fs.createDrive("Device1");
        tryCatchException("Path already exists", () -> fs.createDrive("Device1"));
        tryCatchException("Path not found", () -> fs.get("nonexistent"));
        tryCatchException("Illegal File System Operation", () -> fs.createDrive("Device1\\Device2"));
        tryCatchException("Path not found", () -> fs.remove("nonexistent"));
        tryCatchException("Path not found", () -> fs.move("nonexistent", "newnonexistent"));
        fs.createFolder("Device1\\Folder1");
        fs.createFolder("Device1\\Folder2");
        tryCatchException("Path already exists", () -> fs.move("Device1\\Folder1", "Device1\\Folder2"));
        fs.createDrive("Device2");
        tryCatchException("Illegal File System Operation", () -> fs.move("Device1", "Device2\\Device1"));
        tryCatchException("Path not found", () -> fs.updateTextFile("Device1\\nonexistent", "some content"));
        tryCatchException("Not a text file", () -> fs.updateTextFile("Device1", "some content"));

    }

    @Test
    public void doesItWorkTest() throws FileSystemException {
        FileSystem fs = new FileSystem();
        fs.createDrive("TestDrive");
        Folder folder = fs.createFolder("TestDrive\\Folder");
        fs.createFolder("TestDrive\\Folder\\Folder2");
        fs.createTextFile("TestDrive\\Folder\\Folder2\\TextFile", "Some content");
        TextFile textFile = (TextFile) fs.get("TestDrive\\Folder\\Folder2\\TextFile");
        Assert.assertEquals("Text file didn't contain expected value", "Some content", textFile.getContent());

        fs.move("TestDrive\\Folder\\Folder2", "TestDrive\\Folder\\Folder3");
        textFile = (TextFile) fs.get("TestDrive\\Folder\\Folder3\\TextFile");
        Assert.assertEquals("Text file didn't contain expected value", "Some content", textFile.getContent());

        fs.updateTextFile("TestDrive\\Folder\\Folder3\\TextFile", "Some new content");
        textFile = (TextFile) fs.get("TestDrive\\Folder\\Folder3\\TextFile");
        Assert.assertEquals("Text file didn't contain expected value", "Some new content", textFile.getContent());

        fs.remove("TestDrive\\Folder\\Folder3");
        tryCatchException("Path not found", () -> folder.get("Folder3"));
    }

    private void tryCatchException(String expected, Callable runner) {
        try {
            runner.call();
            Assert.fail("Did not get expected exception: " + expected);
        } catch(Exception e) {
            Assert.assertTrue("Got wrong exception: " + e.getMessage(), e.getMessage().contains(expected));
        }
    }
}
