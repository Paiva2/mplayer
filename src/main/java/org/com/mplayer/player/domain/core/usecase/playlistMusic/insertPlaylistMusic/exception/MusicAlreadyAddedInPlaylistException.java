package org.com.mplayer.player.domain.core.usecase.playlistMusic.insertPlaylistMusic.exception;

import org.com.mplayer.global.ConflictException;

import java.text.MessageFormat;

public class MusicAlreadyAddedInPlaylistException extends ConflictException {
    private final static String DEFAULT = "Music id {0} already added in playlist id {1}";

    public MusicAlreadyAddedInPlaylistException(Long musicId, Long playlistId) {
        super(MessageFormat.format(DEFAULT, musicId, playlistId));
    }
}
