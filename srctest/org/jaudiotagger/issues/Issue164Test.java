package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.generic.DataSource;
import org.jaudiotagger.audio.generic.FileDataSource;
import org.jaudiotagger.audio.mp4.Mp4AtomTree;
import org.jaudiotagger.tag.FieldKey;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test
 */
public class Issue164Test extends AbstractTestCase
{
    @Test
    public void testReadWriteMp4With64BitMDatLength() throws Exception
    {
        File orig = new File("testdata", "test164.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex=null;
        try
        {
            DataSource dataSource = new FileDataSource(orig);
            Mp4AtomTree atomTree = new Mp4AtomTree(dataSource);
            atomTree.printAtomTree();

            File testFile = copyAudioToTmp("test164.m4a");
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertNotNull(af.getTag());
            System.out.println(af.getTag());
            af.getTagOrCreateDefault().setField(FieldKey.PERFORMER,"performer");
            af.commit();
            af = AudioFileIO.read(testFile);
            Assert.assertNotNull(af.getTag());
            System.out.println(af.getTag());
            af.getTagOrCreateDefault().setField(FieldKey.ARTIST,"artist");
            af.commit();
            af = AudioFileIO.read(testFile);
            Assert.assertNotNull(af.getTag());
            System.out.println(af.getTag());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        Assert.assertNull(ex);
    }
}
