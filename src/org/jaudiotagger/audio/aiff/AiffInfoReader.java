
package org.jaudiotagger.audio.aiff;

import org.jaudiotagger.audio.aiff.chunk.*;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.iff.Chunk;
import org.jaudiotagger.audio.iff.ChunkHeader;
import org.jaudiotagger.audio.iff.IffHeaderChunk;
import org.jaudiotagger.logging.Hex;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * Read Aiff chunks, except the ID3 chunk.
 */
public class AiffInfoReader extends AiffChunkReader
{
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.aiff");


    protected GenericAudioHeader read(Path file) throws CannotReadException, IOException
    {
        try(FileChannel fc = FileChannel.open(file))
        {
            logger.config(file + ":Reading AIFF file size:" + fc.size() + " (" + Hex.asHex(fc.size()) + ")");
            AiffAudioHeader aiffAudioHeader = new AiffAudioHeader();
            final AiffFileHeader fileHeader = new AiffFileHeader();
            fileHeader.readHeader(fc, aiffAudioHeader);
            while (fc.position() < fc.size())
            {
                if (!readChunk(fc, aiffAudioHeader))
                {
                    logger.severe("UnableToReadProcessChunk");
                    break;
                }
            }
            calculateBitRate(aiffAudioHeader);
            return aiffAudioHeader;
        }
    }

    /**
     * Calculate bitrate, done it here because requires data from multiple chunks
     *
     * @param info
     * @throws CannotReadException
     */
    private void calculateBitRate(GenericAudioHeader info) throws CannotReadException
    {
        if(info.getAudioDataLength()!=null)
        {
            info.setBitRate((int)(Math.round(info.getAudioDataLength()
                    * Utils.BITS_IN_BYTE_MULTIPLIER / (info.getPreciseTrackLength() * Utils.KILOBYTE_MULTIPLIER))));
        }
    }

    /**
     * Reads an AIFF Chunk.
     *
     * @return {@code false}, if we were not able to read a valid chunk id
     */
    private boolean readChunk(FileChannel fc, AiffAudioHeader aiffAudioHeader) throws IOException
    {
        logger.config("Reading Info Chunk");
        final Chunk chunk;
        final ChunkHeader chunkHeader = new ChunkHeader(ByteOrder.BIG_ENDIAN);
        if (!chunkHeader.readHeader(fc))
        {
            return false;
        }

        logger.config("Reading Next Chunk:" + chunkHeader.getID() + ":starting at:" + chunkHeader.getStartLocationInFile() + ":sizeIncHeader:" + (chunkHeader.getSize() + ChunkHeader.CHUNK_HEADER_SIZE));
        chunk = createChunk(fc, chunkHeader, aiffAudioHeader);
        if (chunk != null)
        {
            if (!chunk.readChunk())
            {
                logger.severe("ChunkReadFail:" + chunkHeader.getID());
                return false;
            }
        }
        else
        {
            fc.position(fc.position() + chunkHeader.getSize());
        }
        IffHeaderChunk.ensureOnEqualBoundary(fc, chunkHeader);
        return true;
    }

    /**
     * Create a chunk. May return {@code null}, if the chunk is not of a valid type.
     *
     * @param fc
     * @param chunkHeader
     * @param aiffAudioHeader
     * @return
     * @throws IOException
     */
    private Chunk createChunk(FileChannel fc, final ChunkHeader chunkHeader, AiffAudioHeader aiffAudioHeader)
    throws IOException {
        final AiffChunkType chunkType = AiffChunkType.get(chunkHeader.getID());
        Chunk chunk;
        if (chunkType != null)
        {
            switch (chunkType)
            {
                case FORMAT_VERSION:
                    chunk = new FormatVersionChunk(chunkHeader, readChunkDataIntoBuffer(fc,chunkHeader), aiffAudioHeader);
                    break;

                case APPLICATION:
                    chunk = new ApplicationChunk(chunkHeader, readChunkDataIntoBuffer(fc,chunkHeader), aiffAudioHeader);
                    break;

                case COMMON:
                    chunk = new CommonChunk(chunkHeader, readChunkDataIntoBuffer(fc,chunkHeader), aiffAudioHeader);
                    break;

                case COMMENTS:
                    chunk = new CommentsChunk(chunkHeader, readChunkDataIntoBuffer(fc,chunkHeader), aiffAudioHeader);
                    break;

                case NAME:
                    chunk = new NameChunk(chunkHeader, readChunkDataIntoBuffer(fc,chunkHeader), aiffAudioHeader);
                    break;

                case AUTHOR:
                    chunk = new AuthorChunk(chunkHeader, readChunkDataIntoBuffer(fc,chunkHeader), aiffAudioHeader);
                    break;

                case COPYRIGHT:
                    chunk = new CopyrightChunk(chunkHeader, readChunkDataIntoBuffer(fc,chunkHeader), aiffAudioHeader);
                    break;

                case ANNOTATION:
                    chunk = new AnnotationChunk(chunkHeader, readChunkDataIntoBuffer(fc,chunkHeader), aiffAudioHeader);
                    break;

                case SOUND:
                    //Dont need to read chunk itself just need size
                    aiffAudioHeader.setAudioDataLength(chunkHeader.getSize());
                    aiffAudioHeader.setAudioDataStartPosition(fc.position());
                    aiffAudioHeader.setAudioDataEndPosition(fc.position() + chunkHeader.getSize());

                    chunk = null;
                    break;

                default:
                    chunk = null;
            }
        }
        else {
            chunk = null;
        }
        return chunk;
    }

}
