package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * Test trying to read non existent mp3 file
 */
public class Issue181Test extends AbstractTestCase {

    @Test
    public void testWavNoNullsInput() {
        File orig = new File("testdata", "test162.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }


        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test162.wav");
            AudioFile f = AudioFileIO.read(testFile);

            System.out.println(f.getTag());
            assertFalse(f.getTag().toString().contains("\0"));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

}