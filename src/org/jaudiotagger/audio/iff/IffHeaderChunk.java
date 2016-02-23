package org.jaudiotagger.audio.iff;

import org.jaudiotagger.audio.generic.DataSource;
import org.jaudiotagger.audio.generic.FileDataSource;
import org.jaudiotagger.audio.generic.Utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.logging.Logger;

/**
 * Common to all IFF formats such as Wav and Aiff
 */
public class IffHeaderChunk
{
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.iff");

    public static int SIGNATURE_LENGTH = 4;
    public static int SIZE_LENGTH = 4;
    public static int TYPE_LENGTH = 4;
    public static int HEADER_LENGTH = SIGNATURE_LENGTH + SIZE_LENGTH + TYPE_LENGTH;

    /**
     * If Size is not even then we skip a byte, because chunks have to be aligned
     *
     * @param raf
     * @param chunkHeader
     * @throws java.io.IOException
     */
    public static void ensureOnEqualBoundary(final RandomAccessFile raf,ChunkHeader chunkHeader) throws IOException
    {
        ensureOnEqualBoundary(new FileDataSource(raf), chunkHeader);
    }

    public static void ensureOnEqualBoundary(FileChannel fc,ChunkHeader chunkHeader) throws IOException
    {
        ensureOnEqualBoundary(new FileDataSource(fc), chunkHeader);
    }

    public static void ensureOnEqualBoundary(DataSource dataSource, ChunkHeader chunkHeader) throws IOException
    {
        if (Utils.isOddLength(chunkHeader.getSize()))
        {
            // Must come out to an even byte boundary unless at end of file
            if(dataSource.position() < dataSource.size())
            {
                logger.config("Skipping Byte because on odd boundary");
                dataSource.position(dataSource.position() + 1);
            }
        }
    }
}
