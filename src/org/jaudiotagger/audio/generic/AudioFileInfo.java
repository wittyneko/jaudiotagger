package org.jaudiotagger.audio.generic;

import org.jaudiotagger.tag.Tag;

/**
 * @author Silvano Riz
 */
public class AudioFileInfo {

    private final GenericAudioHeader genericAudioHeader;
    private Tag tag;

    public AudioFileInfo(GenericAudioHeader genericAudioHeader) {
        this.genericAudioHeader = genericAudioHeader;
    }

    public GenericAudioHeader getGenericAudioHeader() {
        return genericAudioHeader;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

}
