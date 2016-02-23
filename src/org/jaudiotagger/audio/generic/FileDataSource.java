package org.jaudiotagger.audio.generic;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

/**
 * <p> A File {@code DataSource}.
 *
 * @author Silvano Riz
 */
public class FileDataSource extends DataSource {

    static final String NO_NAME = "unknown file";

    private final FileChannel fileChannel;
    private final String fileName;

    public FileDataSource(final File file) throws FileNotFoundException {
        this(new FileInputStream(file).getChannel(), file.getAbsolutePath());
    }

    public FileDataSource(final RandomAccessFile raf, final String fileName) throws FileNotFoundException {
        this(raf.getChannel(), fileName);
    }

    public FileDataSource(final RandomAccessFile raf) throws FileNotFoundException {
        this(raf.getChannel(), null);
    }

    public FileDataSource(final Path path) throws IOException {
        this(FileChannel.open(path), path.toString());
    }

    public FileDataSource(final FileChannel fileChannel, final String fileName) throws FileNotFoundException {
        this.fileChannel = fileChannel;
        this.fileName = fileName != null ? fileName : "unknown file";
    }

    public FileDataSource(final FileChannel fileChannel) throws FileNotFoundException {
        this(fileChannel, null);
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public int read(final ByteBuffer byteBuffer) throws IOException {
        return fileChannel.read(byteBuffer);
    }

    @Override
    public int read(ByteBuffer dst, long position) throws IOException {
        checkPosition(position);
        return fileChannel.read(dst, position);
    }

    @Override
    public long size() throws IOException {
        return fileChannel.size();
    }

    @Override
    public long position() throws IOException {
        return fileChannel.position();
    }

    @Override
    public void position(final long newPosition) throws IOException {
        checkPosition(newPosition);
        fileChannel.position(newPosition);
    }

    @Override
    public void close() throws IOException {
        fileChannel.close();
    }

    @Override
    public String toString() {
        return "FileDataSource{" +
                "file=" + fileName +
                '}';
    }
}
