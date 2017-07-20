package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.FileDataSource;
import org.jaudiotagger.audio.mp4.Mp4AtomTree;
import org.jaudiotagger.tag.FieldKey;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test
 */
public class Issue218Test extends AbstractTestCase
{

    @Test
    public void testReadCorruptMp4ThatLooksLikehas64bitDataLength() throws Exception
    {
        File orig = new File("testdata", "test218.mp4");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex=null;
        try
        {
            File testFile = copyAudioToTmp("test218.mp4");
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertNotNull(af.getTag());
            System.out.println(af.getTag());
            Assert.assertEquals("1968",(af.getTag().getFirst(FieldKey.YEAR)));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        Assert.assertTrue(ex instanceof CannotReadException);
    }

    @Test
    public void testReadCorruptMp4ThatLooksLikehas64bitDataLengthAtomTree() throws Exception
    {
        File orig = new File("testdata", "test218.mp4");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex=null;
        try
        {
            Mp4AtomTree tree = new Mp4AtomTree(new FileDataSource(orig),false);
            tree.printAtomTree();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
    }
}
