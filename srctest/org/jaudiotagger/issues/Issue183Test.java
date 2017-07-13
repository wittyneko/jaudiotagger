package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test
 */
public class Issue183Test extends AbstractTestCase {

    @Test
    public void testReadCorruptOgg() throws Exception {
        File orig = new File("testdata", "test508.ogg");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test508.ogg");
            AudioFileIO.read(testFile);
        } catch (Exception e) {
            assertTrue(e instanceof CannotReadException);
            ex = e;
        }
        assertNotNull(ex);
    }
}
