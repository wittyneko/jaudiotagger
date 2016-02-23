package org.jaudiotagger.audio.generic;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.tag.Tag;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

/**
 * Replacement for AudioFileReader and AudioFileReader2 classes
 */
public abstract class AudioFileReader3 extends AudioFileReader2
{

    /**
     * <p> Reads the audio file header information.
     *
     * @param dataSource The audio file data source
     * @return The audio file header information
     * @throws CannotReadException If the audio file cannot be read
     * @throws IOException If an I/O error occurs
     */
    protected abstract GenericAudioHeader getEncodingInfo(final DataSource dataSource) throws CannotReadException, IOException;

    /**
     * <p> Reads the audio file tag information.
     *
     * @param dataSource The audio file data source
     * @return The audio file tag information
     * @throws CannotReadException If the audio file cannot be read
     * @throws IOException If an I/O error occurs
     */
    protected abstract Tag getTag(final DataSource dataSource) throws CannotReadException, IOException;

    /**
     * <p> Reads the audio file information.
     *
     * @param dataSource The audio file data source
     * @return The audio file information
     * @throws CannotReadException If the audio file cannot be read
     * @throws IOException If an I/O error occurs
     */
    public AudioFileInfo getAudioFileInfo(final DataSource dataSource) throws CannotReadException, IOException {

        // Read the audio header
        dataSource.position(0);
        final GenericAudioHeader genericAudioHeader = getEncodingInfo(dataSource);

        // Read the tag
        dataSource.position(0);
        final Tag tag = getTag(dataSource);

        // Compose the file info and return it.
        final AudioFileInfo audioFileInfo = new AudioFileInfo(genericAudioHeader);
        audioFileInfo.setTag(tag);
        return audioFileInfo;
    }

    @Override
    protected GenericAudioHeader getEncodingInfo(Path path) throws CannotReadException, IOException {
        try (DataSource dataSource = new FileDataSource(path)) {
            return getEncodingInfo(dataSource);
        }
    }

    @Override
    protected GenericAudioHeader getEncodingInfo(RandomAccessFile raf) throws CannotReadException, IOException {
        try (DataSource dataSource = new FileDataSource(raf)) {
            return getEncodingInfo(dataSource);
        }
    }

    @Override
    protected Tag getTag(Path path) throws CannotReadException, IOException {
        try (DataSource dataSource = new FileDataSource(path)) {
            return getTag(dataSource);
        }
    }

    @Override
    protected Tag getTag(RandomAccessFile raf) throws CannotReadException, IOException {
        try (DataSource dataSource = new FileDataSource(raf)) {
            return getTag(dataSource);
        }
    }
}
