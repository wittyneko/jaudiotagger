package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

import static org.junit.Assert.assertTrue;

/**
 * Test Creating Null fields
 */
public class Issue221Test extends AbstractTestCase {


    @Test
    public void testCreateNullMp4FrameTitle() {
        Exception exceptionCaught = null;
        try {
            Mp4Tag tag = new Mp4Tag();
            tag.setField(FieldKey.TITLE, null);
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }


    @Test
    public void testCreateNullOggVorbisFrameTitle() {
        Exception exceptionCaught = null;
        try {
            VorbisCommentTag tag = VorbisCommentTag.createNewTag();
            tag.setField(FieldKey.TITLE, null);
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }


    @Test
    public void testCreateNullID3v23FrameTitle() {
        Exception exceptionCaught = null;
        try {
            ID3v23Tag tag = new ID3v23Tag();
            tag.setField(FieldKey.TITLE, null);
            File file = tempFolder.newFile("01_issue_221_title_v23.mp3");
            FileOutputStream os = new FileOutputStream(file);
            tag.write(os.getChannel(), 0);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }


    @Test
    public void testCreateNullID3v23FrameAlbum() {
        Exception exceptionCaught = null;
        try {
            ID3v23Tag tag = new ID3v23Tag();
            tag.setField(FieldKey.ALBUM, null);
            File file = tempFolder.newFile("02_issue_221_title_v23.mp3");
            FileOutputStream os = new FileOutputStream(file);
            tag.write(os.getChannel(), 0);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }


    @Test
    public void testCreateNullID3v23FrameArtist() {
        Exception exceptionCaught = null;
        try {
            ID3v23Tag tag = new ID3v23Tag();
            tag.setField(FieldKey.ARTIST, null);
            File file = tempFolder.newFile("03_issue_221_title_v23.mp3");
            FileOutputStream os = new FileOutputStream(file);
            tag.write(os.getChannel(), 0);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }


    @Test
    public void testCreateNullID3v23FrameComment() {
        Exception exceptionCaught = null;
        try {
            ID3v23Tag tag = new ID3v23Tag();
            tag.setField(FieldKey.COMMENT, null);
            File file = tempFolder.newFile("04_issue_221_title_v23.mp3");
            FileOutputStream os = new FileOutputStream(file);
            tag.write(os.getChannel(), 0);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }


    @Test
    public void testCreateNullID3v23FrameGenre() {
        Exception exceptionCaught = null;
        try {
            ID3v23Tag tag = new ID3v23Tag();
            tag.setField(FieldKey.GENRE, null);
            File file = tempFolder.newFile("05_issue_221_title_v23.mp3");
            FileOutputStream os = new FileOutputStream(file);
            tag.write(os.getChannel(), 0);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }


    @Test
    public void testCreateNullID3v23FrameTrack() {
        Exception exceptionCaught = null;
        try {
            ID3v23Tag tag = new ID3v23Tag();
            tag.setField(FieldKey.TRACK, null);
            File file = tempFolder.newFile("06_issue_221_title_v23.mp3");
            FileOutputStream os = new FileOutputStream(file);
            tag.write(os.getChannel(), 0);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertTrue(exceptionCaught instanceof IllegalArgumentException);

    }


    @Test
    public void testCreateNullID3v24Frame() {
        Exception exceptionCaught = null;
        try {
            ID3v24Tag tag = new ID3v24Tag();
            tag.setField(FieldKey.TITLE, null);
            File file = tempFolder.newFile("01_issue_221_title_v24.mp3");
            FileOutputStream os = new FileOutputStream(file);
            tag.write(os.getChannel(), 0);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }


    @Test
    public void testCreateNullID3v22Frame() {
        Exception exceptionCaught = null;
        try {
            ID3v22Tag tag = new ID3v22Tag();
            tag.setField(FieldKey.TITLE, null);
            File file = tempFolder.newFile("02_issue_221_title_v24.mp3");
            FileOutputStream os = new FileOutputStream(file);
            tag.write(os.getChannel(), 0);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }
}
