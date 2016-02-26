package org.jaudiotagger.audio.generic;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * <p> Unit test for {@link FileDataSource}
 *
 * @author Silvano Riz
 */
public class FileDataSourceTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testClose() throws Exception {
        File testClose = tempFolder.newFile("testClose.bin");
        FileChannel fileChannel = FileChannel.open(testClose.toPath());
        FileChannel spyFileChannel = Mockito.spy(fileChannel);
        FileDataSource fileDataSource = new FileDataSource(spyFileChannel);
        fileDataSource.close();
        Mockito.verify(spyFileChannel).close();
    }

    @Test
    public void testConstructors() throws Exception {

        File aFile = tempFolder.newFile("testConstructors.bin");

        FileDataSource fileDataSource1 = new FileDataSource(aFile);
        Assert.assertNotNull(fileDataSource1);
        Assert.assertNotNull(aFile.getAbsolutePath(), fileDataSource1.getFileName());

        FileDataSource fileDataSource2 = new FileDataSource(new RandomAccessFile(aFile, "r"), aFile.getAbsolutePath());
        Assert.assertNotNull(fileDataSource2);
        Assert.assertNotNull(aFile.getAbsolutePath(), fileDataSource2.getFileName());

        FileDataSource fileDataSource3 = new FileDataSource(new RandomAccessFile(aFile, "r"));
        Assert.assertNotNull(fileDataSource3);
        Assert.assertNotNull(FileDataSource.NO_NAME, fileDataSource3.getFileName());

        FileDataSource fileDataSource4 = new FileDataSource(aFile.toPath());
        Assert.assertNotNull(fileDataSource4);
        Assert.assertNotNull(aFile.getAbsolutePath(), fileDataSource4.getFileName());

        FileDataSource fileDataSource5 = new FileDataSource(FileChannel.open(aFile.toPath()), aFile.getAbsolutePath());
        Assert.assertNotNull(fileDataSource5);
        Assert.assertNotNull(aFile.getAbsolutePath(), fileDataSource5.getFileName());

        FileDataSource fileDataSource6 = new FileDataSource(FileChannel.open(aFile.toPath()));
        Assert.assertNotNull(fileDataSource6);
        Assert.assertNotNull(FileDataSource.NO_NAME, fileDataSource6.getFileName());
    }

    @Test
    public void testToString() throws Exception{

        File aFile = tempFolder.newFile("testToString.bin");
        FileDataSource fileDataSource1 = new FileDataSource(aFile);
        Assert.assertEquals(String.format("FileDataSource{file=%s}",aFile.getAbsolutePath()), fileDataSource1.toString());

        FileDataSource fileDataSource2 = new FileDataSource(new RandomAccessFile(aFile, "r"));
        Assert.assertEquals(String.format("FileDataSource{file=%s}", FileDataSource.NO_NAME), fileDataSource2.toString());
    }

}