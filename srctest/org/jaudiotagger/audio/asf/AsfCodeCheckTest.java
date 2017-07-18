package org.jaudiotagger.audio.asf;

import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.asf.AsfFieldKey;
import org.jaudiotagger.tag.asf.AsfTag;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
/**
 * This test covers some mistakes that could be made by changing the implementation.<br>
 * For example, constants which are assigned at class loading being <code>null</code>.  
 * 
 * @author Christian Laireiter
 */
public class AsfCodeCheckTest
{
    
    /**
     * Tests the correct implementation of {@link AsfTag}.<br>
     * For example if {@link AsfTag#createField(AsfFieldKey, String)} returns a field whose {@link org.jaudiotagger.tag.TagField#getId()}
     * equals {@link org.jaudiotagger.tag.asf.AsfFieldKey#ALBUM}s }.
     */
    @Test
    public void testAsfTagImpl() throws Exception
    {
        final AsfTag asfTag = new AsfTag();
        assertEquals(asfTag.createField(FieldKey.ALBUM,new String()).getId(), AsfFieldKey.ALBUM.getFieldName());
        assertEquals(asfTag.createField(FieldKey.ARTIST,new String()).getId(), AsfFieldKey.AUTHOR.getFieldName());
        assertEquals(asfTag.createField(FieldKey.COMMENT,new String()).getId(), AsfFieldKey.DESCRIPTION.getFieldName());
        assertEquals(asfTag.createField(FieldKey.GENRE,new String()).getId(), AsfFieldKey.GENRE.getFieldName());
        assertEquals(asfTag.createField(FieldKey.TITLE,new String()).getId(), AsfFieldKey.TITLE.getFieldName());
        assertEquals(asfTag.createField(FieldKey.TRACK,new String()).getId(), AsfFieldKey.TRACK.getFieldName());
        assertEquals(asfTag.createField(FieldKey.YEAR,new String()).getId(),  AsfFieldKey.YEAR.getFieldName());
    }

    /**
     * Tests some constants which must have values.
     */
    @Test
    public void testConstants()
    {
        // UTF16-LE by specification
        assertEquals("ONLY \"UTF-16LE\" text encoding specified", "UTF-16LE", AsfHeader.ASF_CHARSET.name()); // $NON-NLS-1$ //$NON-NLS-2$ 
    }
}
