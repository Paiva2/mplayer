package org.com.mplayer.player.domain.core.usecase.insertMusic.exception;

import org.com.mplayer.ConflictException;

public class MusicAlreadyAddedByUserException extends ConflictException {
    public MusicAlreadyAddedByUserException(String message) {
        super(message);
    }
}
