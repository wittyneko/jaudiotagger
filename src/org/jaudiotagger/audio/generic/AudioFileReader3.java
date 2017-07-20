package org.jaudiotagger.audio.generic;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.NoReadPermissionsException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Replacement for AudioFileReader and AudioFileReader2 classes
 */
public abstract class AudioFileReader3
{

    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.generic");

    protected static final int MINIMUM_SIZE_FOR_VALID_AUDIO_FILE = 100;

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

    public AudioFile read(Path path) throws IOException, CannotReadException, TagException, InvalidAudioFrameException {

        if(logger.isLoggable(Level.CONFIG)) {
            logger.config(ErrorMessage.GENERAL_READ.getMsg(path.toAbsolutePath()));
        }

        if (!Files.exists(path)){
            throw new FileNotFoundException(ErrorMessage.UNABLE_TO_FIND_FILE.getMsg(path.toAbsolutePath()));
        }

        if (!Files.isReadable(path)) {
            logger.warning(Permissions.displayPermissions(path));
            throw new NoReadPermissionsException(ErrorMessage.GENERAL_READ_FAILED_DO_NOT_HAVE_PERMISSION_TO_READ_FILE.getMsg(path));
        }

        AudioFile audioFile = read(new FileDataSource(path));
        audioFile.setFile(path.toFile());
        return audioFile;

    }

    public AudioFile read(File file) throws IOException, CannotReadException, InvalidAudioFrameException, TagException, ReadOnlyFileException {
        return read(file.toPath());
    }

    /**
     * Reads the given file, and return an AudioFile object containing the Tag
     * and the encoding infos present in the file. If the file has no tag, an
     * empty one is returned. If the encodinginfo is not valid , an exception is thrown.
     *
     * @param ds The datasource
     * @exception CannotReadException If anything went bad during the read of this file
     */
    public AudioFile read(final DataSource ds) throws CannotReadException, IOException, InvalidAudioFrameException, TagException {

        if (ds.size() <= MINIMUM_SIZE_FOR_VALID_AUDIO_FILE) {
            throw new CannotReadException(ErrorMessage.GENERAL_READ_FAILED_FILE_TOO_SMALL.getMsg(ds.getName()));
        }

        try {
            ds.position(0);

            GenericAudioHeader info = getEncodingInfo(ds);
            ds.position(0);
            Tag tag = getTag(ds);
            AudioFile audioFile = new AudioFile();
            audioFile.setAudioHeader(info);
            audioFile.setTag(tag);
            return audioFile;

        }catch (Exception e){
            logger.log(Level.SEVERE, ErrorMessage.GENERAL_READ.getMsg(ds.getName()),e);
            throw new CannotReadException(ds.getName() + ":" + e.getMessage(), e);
        } finally{
            try{
                ds.close();
            } catch (Exception ex) {
                logger.log(Level.WARNING, ErrorMessage.GENERAL_READ_FAILED_UNABLE_TO_CLOSE_RANDOM_ACCESS_FILE.getMsg(ds.getName()));
            }
        }
    }

}
