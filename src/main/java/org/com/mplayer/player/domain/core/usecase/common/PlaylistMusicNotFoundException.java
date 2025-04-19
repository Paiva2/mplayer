package org.com.mplayer.player.domain.core.usecase.common;

import org.com.mplayer.global.NotFoundException;

public class PlaylistMusicNotFoundException extends NotFoundException {
    private final static String DEFAULT = "Playlist music not found!";

    public PlaylistMusicNotFoundException() {
        super(DEFAULT);
    }
}
