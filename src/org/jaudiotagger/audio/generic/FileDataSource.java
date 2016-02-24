package org.jaudiotagger.audio.generic;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

/**
 * @author Silvano Riz
 */
public class FileDataSource extends DataSource {

    private final FileChannel fc;

    public FileDataSource(final File f) throws FileNotFoundException {
        this(new FileInputStream(f).getChannel());
    }

    public FileDataSource(final RandomAccessFile raf) throws FileNotFoundException {
        this(raf.getChannel());
    }

    public FileDataSource(final FileChannel fc) throws FileNotFoundException {
        this.fc = fc;
    }

    public FileDataSource(final Path path) throws IOException {
        this(FileChannel.open(path));
    }

    @Override
    public int read(final ByteBuffer byteBuffer) throws IOException {
        return fc.read(byteBuffer);
    }

    @Override
    public int read(ByteBuffer dst, long position) throws IOException {
        return fc.read(dst, position);
    }

    @Override
    public long size() throws IOException {
        return fc.size();
    }

    @Override
    public long position() throws IOException {
        return fc.position();
    }

    @Override
    public void position(final long newPosition) throws IOException {
        fc.position(newPosition);
    }

    @Override
    public void close() throws IOException {
        fc.close();
    }

}
