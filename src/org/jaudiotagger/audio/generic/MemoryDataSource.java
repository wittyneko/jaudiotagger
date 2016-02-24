package org.jaudiotagger.audio.generic;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author Silvano Riz
 */
public class MemoryDataSource extends DataSource {

    private final ByteBuffer data;

    public MemoryDataSource(byte[] data) {
        this.data = ByteBuffer.wrap(data);
    }

    public MemoryDataSource(ByteBuffer buffer) {
        this.data = buffer;
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
    public int read(ByteBuffer dst, long position) throws IOException {
        if (position < 0){
            throw new IllegalArgumentException("Negative position");
        }
        if (position > data.position()){
            return -1;
        }

        int currentPosition = data.position();
        data.position((int)position);
        int bytesRead = read(dst);
        data.position(currentPosition);
        return bytesRead;
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

    private static int longToIntOrThrow(long aLong) {
        if (aLong > Integer.MAX_VALUE || aLong < Integer.MIN_VALUE) {
            throw new RuntimeException("Cannot cast " + aLong + " to an integer. Expected range is (" + Integer.MIN_VALUE + ".." +Integer.MAX_VALUE +")");
        }
        return (int) aLong;
    }

    @Override
    public void close() throws IOException {
        // Nothing to do...
    }
}
