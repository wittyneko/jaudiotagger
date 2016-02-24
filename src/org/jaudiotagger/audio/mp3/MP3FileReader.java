package org.jaudiotagger.audio.mp3;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.generic.AudioFileInfo;
import org.jaudiotagger.audio.generic.AudioFileReader3;
import org.jaudiotagger.audio.generic.DataSource;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;

/**
 * Read Mp3 Info (retrofitted to entagged ,done differently to entagged which is why some methods throw RuntimeException)
 * because done elsewhere
 */
public class MP3FileReader extends AudioFileReader3
{
    protected GenericAudioHeader getEncodingInfo(DataSource dataSource) throws CannotReadException, IOException
    {
        throw new RuntimeException("MP3FileReader.getEncodingInfo should be called");
    }

    protected Tag getTag(DataSource dataSource) throws CannotReadException, IOException
    {
        throw new RuntimeException("MP3FileReader.getEncodingInfo should be called");
    }

    @Override
    public AudioFileInfo getAudioFileInfo(DataSource dataSource) throws CannotReadException, IOException {
        try {
            AudioFile mp3File = read(dataSource);
            AudioFileInfo audioFileInfo = new AudioFileInfo(mp3File.getAudioHeader());
            audioFileInfo.setTag(mp3File.getTag());
            return audioFileInfo;
        }catch (Exception e){
            throw new CannotReadException("Cannot read mp3 file information", e);
        }
    }

    /**
     * @param f
     * @return
     */
    //Override because we read mp3s differently to the entagged code
    public AudioFile read(File f) throws IOException, TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException
    {
        MP3File mp3File = new MP3File(f, MP3File.LOAD_IDV1TAG | MP3File.LOAD_IDV2TAG, true);
        return mp3File;
    }

    public AudioFile read(DataSource dataSource) throws IOException, TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException
    {
        MP3File mp3File = new MP3File(dataSource, MP3File.LOAD_IDV1TAG | MP3File.LOAD_IDV2TAG);
        return mp3File;
    }

    /**
     * Read
     *
     * @param f
     * @return
     * @throws ReadOnlyFileException thrown if the file is not writable
     * @throws org.jaudiotagger.tag.TagException
     * @throws java.io.IOException
     * @throws org.jaudiotagger.audio.exceptions.InvalidAudioFrameException
     */
    public AudioFile readMustBeWritable(File f) throws IOException, TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException
    {
        MP3File mp3File = new MP3File(f, MP3File.LOAD_IDV1TAG | MP3File.LOAD_IDV2TAG, false);
        return mp3File;
    }

}
