package org.jaudiotagger.audio.aiff;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.AudioFileReader3;
import org.jaudiotagger.audio.generic.DataSource;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.tag.Tag;

import java.io.IOException;

/**
 * Reads Audio and Metadata information contained in Aiff file.
 */
public class AiffFileReader extends AudioFileReader3
{

    @Override
    protected GenericAudioHeader getEncodingInfo(DataSource dataSource) throws CannotReadException, IOException
    {
        return new AiffInfoReader(dataSource.getName()).read(dataSource);
    }

    @Override
    protected Tag getTag(DataSource dataSource) throws CannotReadException, IOException
    {
        return new AiffTagReader(dataSource.getName()).read(dataSource);
    }
}
