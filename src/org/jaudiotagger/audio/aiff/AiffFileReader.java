package org.jaudiotagger.audio.aiff;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.*;
import org.jaudiotagger.tag.Tag;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

/**
 * Reads Audio and Metadata information contained in Aiff file.
 */
public class AiffFileReader extends AudioFileReader3
{
    // TODO FIXME, with the merge the two objects are gone and they are instantiated directly in the method
    private AiffInfoReader      ir = new AiffInfoReader();
    private AiffTagReader       im = new AiffTagReader();

    @Override
    protected GenericAudioHeader getEncodingInfo(DataSource dataSource) throws CannotReadException, IOException
    {
        return ir.read(dataSource);
    }

    @Override
    protected Tag getTag(DataSource dataSource) throws CannotReadException, IOException
    {
        return im.read(dataSource);
    }
}
