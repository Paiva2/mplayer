package org.com.mplayer.player.domain.core.usecase.lyrics.getMusicLyrics.exception;

import org.com.mplayer.global.NotFoundException;

public class LyricNotFoundException extends NotFoundException {
    private final static String DEFAULT = "Lyric not found!";

    public LyricNotFoundException() {
        super(DEFAULT);
    }
}
