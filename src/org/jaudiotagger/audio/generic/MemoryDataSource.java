package org.jaudiotagger.audio.generic;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * <p> An in memory {@code DataSource}
 *
 * @author Silvano Riz
 */
public class MemoryDataSource extends DataSource {

    private final ByteBuffer data;

    public MemoryDataSource(final byte[] data) {
        this.data = ByteBuffer.wrap(data);
    }

    public MemoryDataSource(final ByteBuffer data) {
        this.data = data;
    }

    @Override
    public int read(ByteBuffer byteBuffer) throws IOException {
        if (0 == data.remaining() && 0 != byteBuffer.remaining()) {
            return -1;
        }
        int size = Math.min(byteBuffer.remaining(), data.remaining());
        if (byteBuffer.hasArray()) {
            byteBuffer.put(data.array(), data.position(), size);
            data.position(data.position() + size);
        } else {
            byte[] buf = new byte[size];
            data.get(buf);
            byteBuffer.put(buf);
        }
        return size;
    }

    @Override
    public long size() throws IOException {
        return data.capacity();
    }

    @Override
    public long position() throws IOException {
        return data.position();
    }

    @Override
    public void position(long newPosition) throws IOException {
        data.position(longToIntOrThrow(newPosition));
    }

    @Override
    public void close() throws IOException {
        // Nothing to do...
    }

    private static int longToIntOrThrow(long aLong) {
        if (aLong > Integer.MAX_VALUE || aLong < Integer.MIN_VALUE) {
            throw new RuntimeException("Cannot cast " + aLong + " to an integer. Expected range is (" + Integer.MIN_VALUE + ".." +Integer.MAX_VALUE +")");
        }
        return (int) aLong;
    }

}
