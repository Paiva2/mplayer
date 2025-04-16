package org.com.mplayer.player.domain.core.usecase.playlist.createPlaylist.exception;

import org.com.mplayer.global.ConflictException;

public class PlaylistAlreadyExistsException extends ConflictException {
    private final static String DEFAULT = "User already has an playlist with provided name!";

    public PlaylistAlreadyExistsException() {
        super(DEFAULT);
    }
}
