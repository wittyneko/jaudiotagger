package org.jaudiotagger.audio.generic;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.EOFException;
import java.io.File;
import java.nio.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * <p> Functional tests for all the {@link DataSource} implementations
 *
 * @author Silvano Riz
 */
@RunWith(Parameterized.class)
public class DataSourceFunctionalTest {

    private static final byte[] DATA = new byte[]{
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
            0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19,
            0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29
    };

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Parameters(name = "{index}: {0}")
    public static Collection parameters() {
        return Arrays.asList("file", "memory", "memory_no_array");
    }

    private final String testName;
    public DataSourceFunctionalTest(final String testName) {
        this.testName = testName;
    }

    @Test
    public void functionalTest() throws Exception{

        System.err.println("Functional test: " + testName);
        DataSource dataSource = createDataSource();

        // SIZE
        System.out.println("Test size() operation");
        assertEquals(DATA.length, dataSource.size());

        // POSITION
        System.err.println("Test position(long newPosition) and position() operations");
        dataSource.position(0);// Go to the start
        assertEquals(0, dataSource.position());

        dataSource.position(10);// Go somewhere in the middle
        assertEquals(10, dataSource.position());

        dataSource.position(DATA.length); // Go to the end
        assertEquals(DATA.length, dataSource.position());

        Exception expected = null;
        try{
            dataSource.position(DATA.length + 1); // beyond the end -> Exception
        }catch (Exception e){
            expected = e;
        }
        assertNotNull(expected);
        assertTrue(expected instanceof IllegalArgumentException);

        // BOUNDARY SAFE POSITION
        System.out.println("Test size() operation");
        dataSource.boundarySafePosition(-1);
        assertEquals(0, dataSource.position());

        dataSource.boundarySafePosition(10);
        assertEquals(10, dataSource.position());

        dataSource.boundarySafePosition(DATA.length + 1);
        assertEquals(DATA.length, dataSource.position());

        expected = null;
        try{
            dataSource.position(-1); // before the start -> Exception
        }catch (Exception e){
            expected = e;
        }
        assertNotNull(expected);
        assertTrue(expected instanceof IllegalArgumentException);

        // SKIP
        System.out.println("Test skip(long numberOfBytes) operation");
        dataSource.position(0);
        assertEquals(0, dataSource.position());

        dataSource.skip(10);
        assertEquals(10, dataSource.position());

        dataSource.skip(0);
        assertEquals(10, dataSource.position());

        dataSource.skip(-10);
        assertEquals(10, dataSource.position());

        dataSource.skip(DATA.length + 1);
        assertEquals(DATA.length, dataSource.position());

        // READ BYTE
        System.err.println("Test readByte() operation");
        dataSource.position(0);
        assertEquals(0, dataSource.position());

        assertEquals(DATA[0], dataSource.readByte());
        assertEquals(1, dataSource.position());

        dataSource.position(DATA.length -1);
        assertEquals(DATA.length -1, dataSource.position());
        assertEquals(DATA[DATA.length-1], dataSource.readByte());

        expected = null;
        try{
            dataSource.readByte();
        }catch (Exception e){
            expected = e;
        }
        assertNotNull(expected);
        assertTrue(expected instanceof EOFException);

        // READ
        System.out.println("Test read() operation");
        dataSource.position(0);
        assertEquals(0, dataSource.position());
        assertEquals(DATA[0], dataSource.read());
        assertEquals(1, dataSource.position());
        dataSource.position(DATA.length -1);
        assertEquals(DATA.length -1, dataSource.position());
        assertEquals(DATA[DATA.length-1], dataSource.read());
        assertEquals(-1, dataSource.read());

        // READ FULLY
        System.out.println("Test readFully(final byte[] destination) operation");
        final byte[] destinationFully = new byte[]{0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30};
        dataSource.position(0);
        assertEquals(0, dataSource.position());
        dataSource.readFully(destinationFully);
        assertEquals(destinationFully.length, dataSource.position());
        assertArrayEquals(Arrays.copyOfRange(DATA, 0, destinationFully.length), destinationFully);

        dataSource.position(DATA.length - 2);
        expected = null;
        try{
            dataSource.readFully(destinationFully);
        }catch (Exception e){
            expected = e;
        }
        assertNotNull(expected);
        assertTrue(expected instanceof EOFException);

        // READ SEGMENT FULLY
        System.out.println("Test readFully(final byte[] destination, final int offset, final int length) operation");
        final byte[] destinationSegmentFully = new byte[]{0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30};
        dataSource.position(0);
        assertEquals(0, dataSource.position());
        dataSource.readFully(destinationSegmentFully, 2, 5);// In total read 5 bytes.
        //System.out.println("destinationSegmentFully: " + printBytesHexEncoded(destinationSegmentFully));
        assertEquals(5 , dataSource.position());
        assertArrayEquals(Arrays.copyOfRange(DATA, 0, 5), Arrays.copyOfRange(destinationSegmentFully, 2, 7));

        dataSource.position(DATA.length - 2);
        expected = null;
        try{
            dataSource.readFully(destinationFully, 2, 5);
        }catch (Exception e){
            expected = e;
        }
        assertNotNull(expected);
        assertTrue(expected instanceof EOFException);

        // READ INTO ByteBuffer
        System.out.println("Test int read(final ByteBuffer destinationFully) operation");
        dataSource.position(0);
        assertEquals(0, dataSource.position());

        final ByteBuffer destinationByteBuffer = ByteBuffer.allocate(10);
        int readBytes = dataSource.read(destinationByteBuffer);
        System.out.println("destinationByteBuffer array: " + printBytesHexEncoded(destinationByteBuffer.array()));
        assertEquals(10, readBytes);
        assertEquals(10, dataSource.position());
        assertEquals(10, destinationByteBuffer.position());
        assertArrayEquals(Arrays.copyOfRange(DATA, 0, 10), destinationByteBuffer.array());

        destinationByteBuffer.clear();
        dataSource.position(10);
        assertEquals(10, dataSource.position());
        readBytes = dataSource.read(destinationByteBuffer);
        assertEquals(10, readBytes);
        assertEquals(20, dataSource.position());
        assertEquals(10, destinationByteBuffer.position());
        assertArrayEquals(Arrays.copyOfRange(DATA, 10, 20), destinationByteBuffer.array());

        destinationByteBuffer.clear();
        dataSource.position(DATA.length - 2);
        assertEquals(DATA.length - 2, dataSource.position());
        readBytes = dataSource.read(destinationByteBuffer);
        assertEquals(2, readBytes);
        assertEquals(DATA.length, dataSource.position());
        assertEquals(2, destinationByteBuffer.position());
        assertArrayEquals(Arrays.copyOfRange(DATA, DATA.length - 2, DATA.length), Arrays.copyOfRange(destinationByteBuffer.array(), 0, 2));


        // READ INTO ByteBuffer from specific position
        System.out.println("Test int read(final ByteBuffer destinationFully, final long position) operation");
        dataSource.position(0);
        assertEquals(0, dataSource.position());

        final ByteBuffer destinationByteBuffer1 = ByteBuffer.allocate(10);
        int readBytes1 = dataSource.read(destinationByteBuffer1, 10);
        assertEquals(0, dataSource.position());// Datasource position should't change
        assertEquals(10, readBytes1);
        assertEquals(10, destinationByteBuffer1.position());
        assertArrayEquals(Arrays.copyOfRange(DATA, 10, 20), destinationByteBuffer1.array());

        destinationByteBuffer1.clear();
        readBytes1 = dataSource.read(destinationByteBuffer1, DATA.length - 2);
        assertEquals(0, dataSource.position());// Datasource position should't change
        assertEquals(2, readBytes1);
        assertEquals(2, destinationByteBuffer1.position());
        assertArrayEquals(Arrays.copyOfRange(DATA, DATA.length - 2, DATA.length), Arrays.copyOfRange(destinationByteBuffer.array(), 0, 2));

        expected = null;
        try{
            dataSource.read(destinationByteBuffer1, -10);
        }catch (Exception e){
            expected = e;
        }
        assertNotNull(expected);
        assertTrue(expected instanceof IllegalArgumentException);

        expected = null;
        try{
            dataSource.read(destinationByteBuffer1, DATA.length + 1);
        }catch (Exception e){
            expected = e;
        }
        assertNotNull(expected);
        assertTrue(expected instanceof IllegalArgumentException);

        // READ INTO array
        System.out.println("Test int read(final byte[] destinationFully) operation");
        dataSource.position(0);
        assertEquals(0, dataSource.position());

        final byte[] destination = new byte[]{0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30};
        int readBytes2 = dataSource.read(destination);
        assertEquals(10, readBytes2);
        assertEquals(10, dataSource.position());
        assertArrayEquals(Arrays.copyOfRange(DATA, 0, 10), destination);

        dataSource.position(10);
        assertEquals(10, dataSource.position());
        readBytes2 = dataSource.read(destination);
        assertEquals(10, readBytes2);
        assertEquals(20, dataSource.position());
        assertArrayEquals(Arrays.copyOfRange(DATA, 10, 20), destination);

        dataSource.position(DATA.length - 2);
        assertEquals(DATA.length - 2, dataSource.position());
        readBytes2 = dataSource.read(destination);
        assertEquals(2, readBytes2);
        assertEquals(DATA.length, dataSource.position());
        assertArrayEquals(Arrays.copyOfRange(DATA, DATA.length - 2, DATA.length), Arrays.copyOfRange(destination, 0, 2));

        dataSource.position(DATA.length);
        assertEquals(DATA.length, dataSource.position());
        readBytes2 = dataSource.read(destination);
        assertEquals(-1, readBytes2);

        // READ INTO array segment
        System.out.println("Test int read(final byte[] destinationFully, final int offset, final int length) operation");
        dataSource.position(0);
        assertEquals(0, dataSource.position());

        final byte[] destinationSegment = new byte[]{0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30};
        int readBytes3 = dataSource.read(destinationSegment, 2, 5);
        assertEquals(5, readBytes3);
        assertEquals(5, dataSource.position());
        assertArrayEquals(Arrays.copyOfRange(DATA, 0, 5), Arrays.copyOfRange(destinationSegment, 2, 7));

        dataSource.position(DATA.length - 2);
        assertEquals(DATA.length - 2, dataSource.position());
        readBytes3 = dataSource.read(destinationSegment, 2, 5);
        assertEquals(2, readBytes3);
        assertEquals(DATA.length, dataSource.position());
        assertArrayEquals(Arrays.copyOfRange(DATA, DATA.length - 2, DATA.length), Arrays.copyOfRange(destinationSegment, 2, 4));

        dataSource.position(DATA.length);
        assertEquals(DATA.length, dataSource.position());
        readBytes3 = dataSource.read(destinationSegment, 2, 5);
        assertEquals(-1, readBytes3);

        // CLOSE
        dataSource.close();

    }

    private DataSource createDataSource(){

        if ("file".equals(testName)){
            try {
                final File dataFile = tempFolder.newFile();
                Files.write(dataFile.toPath(), DATA, StandardOpenOption.WRITE);
                return new FileDataSource(dataFile);
            }catch (Exception e){
                throw new IllegalStateException("Unable to initialize test case: " + testName, e);
            }
        }else if ("memory".equals(testName)){
            return new MemoryDataSource(DATA);
        }else if ("memory_no_array".equals(testName)){

            final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(DATA.length);
            byteBuffer.put(DATA);

            return new MemoryDataSource(byteBuffer);
        }
        throw new IllegalStateException("Unknown test case: " + testName);
    }

    public static String printByteHexEncoded(byte b){
        return String.format("0x%02x", b & 0xff);
    }

    public static String printBytesHexEncoded(final byte[] byteArray){

        if(byteArray == null){
            return "Null array";
        }

        StringBuilder sb = new StringBuilder(byteArray.length * 2);
        sb.append("[");
        int i=0;
        while (i<byteArray.length) {
            sb.append(printByteHexEncoded(byteArray[i]));
            i++;
            if (i<byteArray.length){
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();

    }
}