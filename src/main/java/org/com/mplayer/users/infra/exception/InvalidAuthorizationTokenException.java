package org.com.mplayer.users.infra.exception;

import org.com.mplayer.ForbiddenException;

public class InvalidAuthorizationTokenException extends ForbiddenException {
    public InvalidAuthorizationTokenException(String message) {
        super(message);
    }
}
