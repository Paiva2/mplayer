package org.com.mplayer.users.domain.core.usecase.common.exception.core;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
