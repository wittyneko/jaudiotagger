/*
 * Entagged Audio Tag library
 * Copyright (c) 2004-2005 Christian Laireiter <liree@web.de>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.FileHeader;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.util.Utils;
import org.jaudiotagger.audio.generic.DataSource;

import java.io.IOException;
import java.math.BigInteger;

/**
 * Reads and interprets the data of the file header. <br>
 * 
 * @author Christian Laireiter
 */
public class FileHeaderReader implements ChunkReader {

    /**
     * The GUID this reader {@linkplain #getApplyingIds() applies to}
     */
    private final static GUID[] APPLYING = { GUID.GUID_FILE };

    /**
     * Should not be used for now.
     */
    protected FileHeaderReader() {
        // NOTHING toDo
    }

    /**
     * {@inheritDoc}
     */
    public boolean canFail() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public GUID[] getApplyingIds() {
        return APPLYING.clone();
    }

    /**
     * {@inheritDoc}
     */
    public Chunk read(final GUID guid, final DataSource dataSource,
            final long chunkStart) throws IOException {
        final BigInteger chunkLen = Utils.readBig64(dataSource);
        // Skip client GUID.
        dataSource.skip(16);
        final BigInteger fileSize = Utils.readBig64(dataSource);
        // fileTime in 100 ns since midnight of 1st january 1601 GMT
        final BigInteger fileTime = Utils.readBig64(dataSource);

        final BigInteger packageCount = Utils.readBig64(dataSource);

        final BigInteger timeEndPos = Utils.readBig64(dataSource);
        final BigInteger duration = Utils.readBig64(dataSource);
        final BigInteger timeStartPos = Utils.readBig64(dataSource);

        final long flags = Utils.readUINT32(dataSource);

        final long minPkgSize = Utils.readUINT32(dataSource);
        final long maxPkgSize = Utils.readUINT32(dataSource);
        final long uncompressedFrameSize = Utils.readUINT32(dataSource);

        final FileHeader result = new FileHeader(chunkLen, fileSize, fileTime,
                packageCount, duration, timeStartPos, timeEndPos, flags,
                minPkgSize, maxPkgSize, uncompressedFrameSize);
        result.setPosition(chunkStart);
        return result;
    }

}