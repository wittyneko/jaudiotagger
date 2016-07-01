package org.jaudiotagger.audio.aiff;

import junit.framework.TestCase;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.DataSource;
import org.jaudiotagger.audio.generic.FileDataSource;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.jaudiotagger.audio.aiff.AiffType.AIFC;
import static org.jaudiotagger.audio.aiff.AiffType.AIFF;

/**
 * AiffFileHeader tests.
 */
public class AiffFileHeaderTest extends TestCase {

    public void testValidAIFF() throws IOException, CannotReadException {
        final int size = 1234;
        final File aiffFile = createAIFF("FORM", "AIFF", size);

        try(DataSource dataSource = new FileDataSource(aiffFile.toPath())) {
            final AiffFileHeader header = new AiffFileHeader();
            final AiffAudioHeader aiffAudioHeader = new AiffAudioHeader();
            final long remainingBytes = header.readHeader(dataSource, aiffAudioHeader);
            assertEquals((long)(size - 4), remainingBytes);
            assertEquals(AIFF, aiffAudioHeader.getFileType());
        }

        aiffFile.delete();
    }

    public void testInvalidFormatType() throws IOException, CannotReadException {
        final int size = 5762;
        final File aiffFile = createAIFF("FORM", "COOL", size);

        try(DataSource dataSource = new FileDataSource(aiffFile.toPath())) {
            new AiffFileHeader().readHeader(dataSource, new AiffAudioHeader());
            fail("Expected " + CannotReadException.class.getSimpleName());
        } catch (CannotReadException e) {
            // expected this
        }

        aiffFile.delete();
    }

    public void testInvalidFormat1() throws IOException, CannotReadException {
        final int size = 34242;
        final File aiffFile = createAIFF("FURM", "AIFF", size);

        try(DataSource dataSource = new FileDataSource(aiffFile.toPath())) {
            new AiffFileHeader().readHeader(dataSource, new AiffAudioHeader());
            fail("Expected " + CannotReadException.class.getSimpleName());
        } catch (CannotReadException e) {
            // expected this
        }

        aiffFile.delete();
    }


    public void testInvalidFormat2() throws IOException, CannotReadException {
        final int size = 34234;
        final File aiffFile = createAIFF("FORMA", "AIFF", size);

        try(DataSource dataSource = new FileDataSource(aiffFile.toPath())) {
            new AiffFileHeader().readHeader(dataSource, new AiffAudioHeader());
            fail("Expected " + CannotReadException.class.getSimpleName());
        } catch (CannotReadException e) {
            // expected this
        }

        aiffFile.delete();
    }

    public void testValidAIFC() throws IOException, CannotReadException {
        final int size = 3452;
        final File aiffFile = createAIFF("FORM", "AIFC", size);

        try(DataSource dataSource = new FileDataSource(aiffFile.toPath())) {
            final AiffFileHeader header = new AiffFileHeader();
            final AiffAudioHeader aiffAudioHeader = new AiffAudioHeader();
            final long remainingBytes = header.readHeader(dataSource, aiffAudioHeader);
            assertEquals((long)(size - 4), remainingBytes);
            assertEquals(AIFC, aiffAudioHeader.getFileType());
        }

        aiffFile.delete();
    }

    private static File createAIFF(final String form, final String formType, final int size) throws IOException {
        final File tempFile = File.createTempFile(AiffFileHeaderTest.class.getSimpleName(), ".aif");
        tempFile.deleteOnExit();

        try (final DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile))) {
            // write format
            out.write(form.getBytes(StandardCharsets.US_ASCII));
            // write size
            out.writeInt(size);
            // write format type
            out.write(formType.getBytes(StandardCharsets.US_ASCII));
            // write remaining random data
            final byte[] remainingData = new byte[size - formType.length()];
            final Random random = new Random();
            random.nextBytes(remainingData);
            out.write(remainingData);
        }
        return tempFile;
    }


}
