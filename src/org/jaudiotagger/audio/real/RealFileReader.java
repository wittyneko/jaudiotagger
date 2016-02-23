package org.jaudiotagger.audio.real;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.AudioFileReader3;
import org.jaudiotagger.audio.generic.DataSource;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.IOException;

/**
 * Real Media File Format: Major Chunks: .RMF PROP MDPR CONT DATA INDX
 */
public class RealFileReader extends AudioFileReader3
{

    @Override
    protected GenericAudioHeader getEncodingInfo(DataSource dataSource) throws CannotReadException, IOException
    {
        final GenericAudioHeader rv = new GenericAudioHeader();
        final RealChunk prop = findPropChunk(dataSource);
        final DataSource propDataSource = prop.getDataInputStream();
        final int objVersion = Utils.readUint16(propDataSource);
        if (objVersion == 0)
        {
            final long maxBitRate = Utils.readUint32(propDataSource) / 1000;
            final long avgBitRate = Utils.readUint32(propDataSource) / 1000;
            final long maxPacketSize = Utils.readUint32(propDataSource);
            final long avgPacketSize = Utils.readUint32(propDataSource);
            final long packetCnt = Utils.readUint32(propDataSource);
            final int duration = (int)Utils.readUint32(propDataSource) / 1000;
            final long preroll = Utils.readUint32(propDataSource);
            final long indexOffset = Utils.readUint32(propDataSource);
            final long dataOffset = Utils.readUint32(propDataSource);
            final int numStreams = Utils.readUint16(propDataSource);
            final int flags = Utils.readUint16(propDataSource);
            rv.setBitRate((int) avgBitRate);
            rv.setPreciseLength(duration);
            rv.setVariableBitRate(maxBitRate != avgBitRate);
        }
        return rv;
    }

    private RealChunk findPropChunk(DataSource dataSource) throws IOException, CannotReadException
    {
        final RealChunk rmf = RealChunk.readChunk(dataSource);
        final RealChunk prop = RealChunk.readChunk(dataSource);
        return prop;
    }

    private RealChunk findContChunk(DataSource dataSource) throws IOException, CannotReadException
    {
        final RealChunk rmf = RealChunk.readChunk(dataSource);
        final RealChunk prop = RealChunk.readChunk(dataSource);
        RealChunk rv = RealChunk.readChunk(dataSource);
        while (!rv.isCONT()) rv = RealChunk.readChunk(dataSource);
        return rv;
    }

    @Override
    protected Tag getTag(DataSource dataSource) throws CannotReadException, IOException
    {
        final RealChunk cont = findContChunk(dataSource);
        final DataSource contDataSource = cont.getDataInputStream();
        final String title = Utils.readString(contDataSource, Utils.readUint16(contDataSource));
        final String author = Utils.readString(contDataSource, Utils.readUint16(contDataSource));
        final String copyright = Utils.readString(contDataSource, Utils.readUint16(contDataSource));
        final String comment = Utils.readString(contDataSource, Utils.readUint16(contDataSource));
        final RealTag rv = new RealTag();
        // NOTE: frequently these fields are off-by-one, thus the crazy
        // logic below...
        try
        {
            rv.addField(FieldKey.TITLE,(title.length() == 0 ? author : title));
            rv.addField(FieldKey.ARTIST, title.length() == 0 ? copyright : author);
            rv.addField(FieldKey.COMMENT,comment);
        }
        catch(FieldDataInvalidException fdie)
        {
            throw new RuntimeException(fdie);
        }
        return rv;
    }

}

