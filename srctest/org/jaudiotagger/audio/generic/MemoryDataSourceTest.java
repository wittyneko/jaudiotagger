package org.jaudiotagger.audio.generic;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * <p> Unit test for {@link MemoryDataSource}
 *
 * @author Silvano Riz
 */
public class MemoryDataSourceTest {

    @Test
    public void testConstructors(){

        MemoryDataSource memoryDataSource1 = new MemoryDataSource(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09});
        Assert.assertNotNull(memoryDataSource1);

        MemoryDataSource memoryDataSource2 = new MemoryDataSource(ByteBuffer.allocate(10));
        Assert.assertNotNull(memoryDataSource2);

    }

    @Test
    public void testlongToIntOrThrow(){

        Assert.assertEquals(Integer.MAX_VALUE, MemoryDataSource.longToIntOrThrow(Integer.MAX_VALUE));
        Assert.assertEquals(10, MemoryDataSource.longToIntOrThrow(10L));
        Assert.assertEquals(Integer.MIN_VALUE, MemoryDataSource.longToIntOrThrow(Integer.MIN_VALUE));

        Exception expected = null;
        try{
            MemoryDataSource.longToIntOrThrow(2147483648L);// Integer.MAX_VALUE + 1
        }catch (Exception e){
            expected = e;
        }
        Assert.assertNotNull(expected);
        Assert.assertTrue(expected instanceof IllegalStateException);

        expected = null;
        try{
            MemoryDataSource.longToIntOrThrow(-2147483649L);// Integer.MIN_VALUE - 1
        }catch (Exception e){
            expected = e;
        }
        Assert.assertNotNull(expected);
        Assert.assertTrue(expected instanceof IllegalStateException);
    }

}