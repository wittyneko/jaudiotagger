package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.*;
import org.jaudiotagger.audio.asf.util.Utils;
import org.jaudiotagger.audio.generic.DataSource;

import java.io.IOException;
import java.math.BigInteger;

/**
 * Reads an interprets &quot;Metadata Object&quot;, &quot;Metadata Library
 * Object&quot; and &quot;Extended Content Description&quot; of ASF files.<br>
 * 
 * @author Christian Laireiter
 */
public class MetadataReader implements ChunkReader {

    /**
     * The GUID this reader {@linkplain #getApplyingIds() applies to}
     */
    private final static GUID[] APPLYING = {
            ContainerType.EXTENDED_CONTENT.getContainerGUID(),
            ContainerType.METADATA_OBJECT.getContainerGUID(),
            ContainerType.METADATA_LIBRARY_OBJECT.getContainerGUID() };

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
            final long streamPosition) throws IOException {
        final BigInteger chunkLen = Utils.readBig64(dataSource);

        final MetadataContainer result = new MetadataContainer(guid,
                streamPosition, chunkLen);
        // isExtDesc will be set to true, if a extended content description
        // chunk is read
        // otherwise it is a metadata object, there are only slight differences
        final boolean isExtDesc = result.getContainerType() == ContainerType.EXTENDED_CONTENT;
        final int recordCount = Utils.readUINT16(dataSource);
        for (int i = 0; i < recordCount; i++) {
            int languageIndex = 0;
            int streamNumber = 0;
            if (!isExtDesc) {
                /*
                 * Metadata objects have a language index and a stream number
                 */
                languageIndex = Utils.readUINT16(dataSource);
                assert languageIndex >= 0
                        && languageIndex < MetadataDescriptor.MAX_LANG_INDEX;
                assert result.getContainerType() == ContainerType.METADATA_LIBRARY_OBJECT
                        || languageIndex == 0;
                streamNumber = Utils.readUINT16(dataSource);
                assert streamNumber >= 0
                        && streamNumber <= MetadataDescriptor.MAX_STREAM_NUMBER;
            }
            final int nameLen = Utils.readUINT16(dataSource);
            String recordName = null;
            if (isExtDesc) {
                recordName = Utils.readFixedSizeUTF16Str(dataSource, nameLen);
            }
            final int dataType = Utils.readUINT16(dataSource);
            assert dataType >= 0 && dataType <= 6;
            final long dataLen = isExtDesc ? Utils.readUINT16(dataSource) : Utils
                    .readUINT32(dataSource);
            assert dataLen >= 0;
            assert result.getContainerType() == ContainerType.METADATA_LIBRARY_OBJECT
                    || dataLen <= MetadataDescriptor.DWORD_MAXVALUE;
            if (!isExtDesc) {
                recordName = Utils.readFixedSizeUTF16Str(dataSource, nameLen);
            }
            final MetadataDescriptor descriptor = new MetadataDescriptor(result
                    .getContainerType(), recordName, dataType, streamNumber,
                    languageIndex);
            switch (dataType) {
            case MetadataDescriptor.TYPE_STRING:
                descriptor.setStringValue(Utils.readFixedSizeUTF16Str(dataSource,
                        (int) dataLen));
                break;
            case MetadataDescriptor.TYPE_BINARY:
                descriptor.setBinaryValue(Utils.readBinary(dataSource, dataLen));
                break;
            case MetadataDescriptor.TYPE_BOOLEAN:
                assert isExtDesc && dataLen == 4 || !isExtDesc && dataLen == 2;
                descriptor.setBooleanValue(readBoolean(dataSource, (int) dataLen));
                break;
            case MetadataDescriptor.TYPE_DWORD:
                assert dataLen == 4;
                descriptor.setDWordValue(Utils.readUINT32(dataSource));
                break;
            case MetadataDescriptor.TYPE_WORD:
                assert dataLen == 2;
                descriptor.setWordValue(Utils.readUINT16(dataSource));
                break;
            case MetadataDescriptor.TYPE_QWORD:
                assert dataLen == 8;
                descriptor.setQWordValue(Utils.readUINT64(dataSource));
                break;
            case MetadataDescriptor.TYPE_GUID:
                assert dataLen == GUID.GUID_LENGTH;
                descriptor.setGUIDValue(Utils.readGUID(dataSource));
                break;
            default:
                // Unknown, hopefully the convention for the size of the
                // value
                // is given, so we could read it binary
                descriptor.setStringValue("Invalid datatype: "
                        + new String(Utils.readBinary(dataSource, dataLen)));
            }
            result.addDescriptor(descriptor);
        }
        return result;
    }

    /**
     * Reads the given amount of bytes and checks the last byte, if its equal to
     * one or zero (true / false).<br>
     * All other bytes must be zero. (if assertions enabled).
     * 
     * @param dataSource
     *            stream to read from.
     * @param bytes
     *            amount of bytes
     * @return <code>true</code> or <code>false</code>.
     * @throws IOException
     *             on I/O Errors
     */
    private boolean readBoolean(final DataSource dataSource, final int bytes)
            throws IOException {
        final byte[] tmp = new byte[bytes];
        dataSource.read(tmp);
        boolean result = false;
        for (int i = 0; i < bytes; i++) {
            if (i == bytes - 1) {
                result = tmp[i] == 1;
                assert tmp[i] == 0 || tmp[i] == 1;
            } else {
                assert tmp[i] == 0;
            }
        }
        return result;
    }

}
