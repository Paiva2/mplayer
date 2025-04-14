package org.com.mplayer.users.domain.core.usecase.user.authUser.exception;

import org.com.mplayer.global.ForbiddenException;

public class InvalidCredentialsException extends ForbiddenException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
