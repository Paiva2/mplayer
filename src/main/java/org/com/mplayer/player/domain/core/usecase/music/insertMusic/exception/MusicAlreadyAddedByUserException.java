package org.com.mplayer.player.domain.core.usecase.music.insertMusic.exception;

import org.com.mplayer.global.ConflictException;

public class MusicAlreadyAddedByUserException extends ConflictException {
    public MusicAlreadyAddedByUserException(String message) {
        super(message);
    }
}
