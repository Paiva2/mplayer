package org.com.mplayer.player.domain.core.usecase.common.exception;

import org.com.mplayer.global.NotFoundException;

public class PlaylistNotFoundException extends NotFoundException {
    private final static String DEFAULT = "Playlist not found!";

    public PlaylistNotFoundException() {
        super(DEFAULT);
    }
}
