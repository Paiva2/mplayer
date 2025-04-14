package org.com.mplayer.users.infra.exception;

import org.com.mplayer.global.ForbiddenException;

public class InvalidAuthorizationTokenException extends ForbiddenException {
    public InvalidAuthorizationTokenException(String message) {
        super(message);
    }
}
