package org.com.mplayer.users.domain.core.usecase.user.authUser.exception;

import org.com.mplayer.ForbiddenException;

public class InvalidCredentialsException extends ForbiddenException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
