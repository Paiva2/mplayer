package org.com.mplayer.player.domain.core.enums;

public enum EFileType {
    MP3("mp3"),
    FLAC("flac");

    private String type;

    EFileType(String type) {
        this.type = type;
    }

    public String getGetTypeLower() {
        return this.type;
    }
}
