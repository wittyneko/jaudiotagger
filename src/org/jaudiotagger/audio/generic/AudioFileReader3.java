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

    protected abstract GenericAudioHeader getEncodingInfo(DataSource dataSource) throws CannotReadException, IOException;

    protected abstract Tag getTag(DataSource dataSource) throws CannotReadException, IOException;

    public AudioFileInfo getAudioFileInfo(DataSource dataSource) throws CannotReadException, IOException {
        GenericAudioHeader genericAudioHeader = getEncodingInfo(dataSource);
        Tag tag = getTag(dataSource);

        AudioFileInfo audioFileInfo = new AudioFileInfo(genericAudioHeader);
        audioFileInfo.setTag(tag);

        return audioFileInfo;
    }

    @Override
    protected GenericAudioHeader getEncodingInfo(Path path) throws CannotReadException, IOException {
        return getEncodingInfo(new FileDataSource(path));
    }

    @Override
    protected GenericAudioHeader getEncodingInfo(RandomAccessFile raf) throws CannotReadException, IOException {
        return getEncodingInfo(new FileDataSource(raf));
    }

    @Override
    protected Tag getTag(Path path) throws CannotReadException, IOException {
        return getTag(new FileDataSource(path));
    }

    @Override
    protected Tag getTag(RandomAccessFile file) throws CannotReadException, IOException {
        return getTag(new FileDataSource(file));
    }
}
