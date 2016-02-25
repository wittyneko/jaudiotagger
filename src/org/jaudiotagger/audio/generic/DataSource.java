package org.jaudiotagger.audio.generic;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * <p> A data source providing random access to the binary data.
 *
 * @author Silvano Riz
 */
public abstract class DataSource implements Closeable {

    /**
     * <p> Reads a sequence of bytes from the source starting from the current position into the specified destination.
     *     There is no guarantee that the buffer is filled completely and the method returns the number of bytes actually read.
     *     The position is updated accordingly to the number of bytes read.
     *
     * @param destination Where the bytes are read into.
     * @return The actual number of bytes read. -1 if the an EOF is encountered in either the source or the destination
     * @throws IOException If an I/O error occurs
     */
    public abstract int read(final ByteBuffer destination) throws IOException;

    /**
     * <p> Returns the size (total number of bytes) contained in the data source.
     *
     * @return The size of the data source.
     * @throws IOException If an I/O error occurs
     */
    public abstract long size() throws IOException;

    /**
     * <p> Returns the current reading position.
     *
     * @return The current reading position.
     * @throws IOException If an I/O error occurs
     */
    public abstract long position() throws IOException;

    /**
     * <p> Sets the reading position to the new position.
     *
     * @param newPosition The new reading position. Must be between 0 and the datasource size
     * @throws IOException IOException If an I/O error occurs
     */
    public abstract void position(final long newPosition) throws IOException;

    /**
     * <p> Skips a number of bytes and updates the read position to the new position.
     *     Differently from {@link #position(long)} the method is not throwing an exception if the numberOfBytes
     *     specified is negative or if the resulting new position is grater than the source size.
     *     A negative numberOfBytes considered a no-op and no byte are skipped.
     *     If the current position plus the numberOfBytes is greater that the size, the position will be moved at the
     *     end of the data source. Subsequent reads will get an EOF.
     *
     * @param numberOfBytes The number of bytes to skip
     * @return the number of bytes actually skipped.
     * @throws IOException If an I/O error occurs
     */
    public long skip(final long numberOfBytes) throws IOException{
        if (numberOfBytes <= 0){
            return 0;
        }
        final long position = position();
        long newPosition = position + numberOfBytes;
        if (newPosition > size()){
            newPosition = size();
        }
        position(newPosition);
        return newPosition - position;
    }

    /**
     * <p> Reads a sequence of bytes from the source starting from the specified position into the specified destination.
     *     There is no guarantee that the buffer is filled completely and the method returns the number of bytes actually read.
     *     The position is NOT updated.
     *
     * @param destination Where the bytes are read into.
     * @param position The position from where start reading. It must be positive.
     * @return The actual number of bytes read. -1 if the an EOF is encountered in either the source or the destination
     * @throws IOException If an I/O error occurs
     */
    public int read(final ByteBuffer destination, final long position) throws IOException {
        if (position < 0){
            throw new IllegalArgumentException("Negative position");
        }
        if (position > position()){
            return -1;
        }

        final long currentPosition = position();
        position((int)position);
        final int bytesRead = read(destination);
        position(currentPosition);
        return bytesRead;
    }

    /**
     * <p> Reads a sequence of bytes from the source starting from the current position into the specified destination.
     *     There is no guarantee that the destination array is filled completely and the method returns the number of bytes actually read.
     *     The position is updated accordingly to the number of bytes read.
     *
     * @param destination Where the bytes are read into.
     * @return The actual number of bytes read. -1 if the an EOF is encountered.
     * @throws IOException If an I/O error occurs
     */
    public int read(final byte[] destination) throws IOException {
        return read(ByteBuffer.wrap(destination));
    }

    /**
     * <p> Reads {@code length} bytes from the source starting from the current position into the specified destination starting from {@code offset}.
     *     There is no guarantee that the destination array section is filled completely and the method returns the number of bytes actually read.
     *     The position is updated accordingly to the number of bytes read.
     *
     * @param destination Where the bytes are read into.
     * @param offset From where to start writing into the destination
     * @param length How many bytes to write
     * @return The actual number of bytes read. -1 if the an EOF is encountered.
     * @throws IOException If an I/O error occurs
     */
    public int read(final byte[] destination, final int offset, final int length) throws IOException {
        return read(ByteBuffer.wrap(destination, offset, length));
    }

    /**
     * <p> Reads the next byte from the datasource. An EOF exception is thrown if the data source position is at the end.
     *     The position is updated accordingly to the number of bytes read.
     *
     * @return The byte read.
     * @throws IOException If an I/O error occurs
     */
    public byte readByte() throws IOException {
        final int b = read();
        if (b < 0) {
            throw new EOFException();
        }
        return (byte)(b);
    }

    /**
     * <p> Reads the next byte from the data source, returning -1 if an EOF state is found.
     *     The position is updated accordingly to the number of bytes read.
     *
     * @return The byte read.
     * @throws IOException If an I/O error occurs
     */
    public int read() throws IOException {
        final byte b[] = new byte[1];
        int bytesRead = read(b);
        if (bytesRead == 1){
            return b[0] & 0xFF;
        }
        return bytesRead;
    }

    /**
     * <p> Reads a sequence of bytes from the source starting from the current position into the specified destination.
     *     The method guarantees that the destination is completely filled. If the end of the source is reached, an
     *     EOFException is thrown.
     *     The position is updated accordingly to the number of bytes read.
     *
     * @param destination Where the bytes are read into.
     * @throws IOException If an I/O error occurs
     */
    public void readFully(final byte[] destination) throws IOException {
        readFully(destination, 0, destination.length);
    }

    /**
     * <p> Reads {@code length} number of bytes from the source starting from the current position into the specified destination
     *     starting from {@code offset}.
     *     The method guarantees that the destination section is completely filled. If the end of the source is reached, an
     *     EOFException is thrown.
     *     The position is updated accordingly to the number of bytes read.
     *
     * @param destination Where the bytes are read into.
     * @param offset From where to start writing into the destination
     * @param length How many bytes to write
     * @throws IOException If an I/O error occurs
     */
    public void readFully(final byte[] destination, final int offset, final int length) throws IOException {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(destination, offset, length);
        while(byteBuffer.hasRemaining()){
            int bytesRead = read(byteBuffer);
            if (bytesRead == -1){
                throw new EOFException("");
            }
        }
    }
}
