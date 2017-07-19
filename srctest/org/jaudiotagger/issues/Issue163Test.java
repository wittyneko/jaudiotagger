package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Test trying to read non existent mp3 file
 */
public class Issue163Test extends AbstractTestCase
{
    @Test
    public void testReadProblemArtwork()    {
        Exception e=null;
        try
        {
            File orig = new File("testdata", "test163.mp3");
            MP3File f = (MP3File)AudioFileIO.read(orig);
            System.out.println(f.displayStructureAsPlainText());
        }
        catch(Exception ex)
        {
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertTrue(e instanceof FileNotFoundException);
    }



}