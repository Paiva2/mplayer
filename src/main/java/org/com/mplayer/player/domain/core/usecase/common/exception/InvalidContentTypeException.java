package org.com.mplayer.player.domain.core.usecase.common.exception;

public class InvalidContentTypeException extends RuntimeException {
    public InvalidContentTypeException(String message) {
        super(message);
    }
}
