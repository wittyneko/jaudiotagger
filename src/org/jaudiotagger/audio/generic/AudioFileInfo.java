package org.jaudiotagger.audio.generic;

import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.Tag;

/**
 * @author Silvano Riz
 */
public class AudioFileInfo {

    private final AudioHeader audioHeader;
    private Tag tag;

    public AudioFileInfo(AudioHeader audioHeader) {
        this.audioHeader = audioHeader;
    }

    public AudioHeader getAudioHeader() {
        return audioHeader;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

}
