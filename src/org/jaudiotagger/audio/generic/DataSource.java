package org.jaudiotagger.audio.generic;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author Silvano Riz
 */
public abstract class DataSource implements Closeable {

    public abstract int read(final ByteBuffer byteBuffer) throws IOException;

    public abstract long size() throws IOException;

    public abstract long position() throws IOException;

    public abstract void position(final long newPosition) throws IOException;

    public long skip(long numberOfBytes) throws IOException{
        if (numberOfBytes <= 0){
            return 0;
        }
        long position = position();
        long newPosition = position + numberOfBytes;
        if (newPosition > size()){
            newPosition = size();
        }
        position(newPosition);
        return newPosition - position;
    }

    public int read(byte[] b) throws IOException {
        return read(ByteBuffer.wrap(b));
    }

    public byte readByte() throws IOException{
        int b = read();
        if (b < 0) {
            throw new EOFException();
        }
        return (byte)(b);
    }

    public int read() throws IOException {
        byte b[] = new byte[1];
        int bytesRead = read(b);
        if (bytesRead == 1){
            return b[0] & 0xFF;
        }
        return bytesRead;
    }

    public void readFully(byte[] b) throws IOException {
        readFully(b, 0, b.length);
    }

    public void readFully(byte[] b, int offset, int length) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(b, offset, length);
        while(byteBuffer.hasRemaining()){
            int bytesRead = read(byteBuffer);
            if (bytesRead == -1){
                throw new EOFException("");
            }
        }
    }
}
