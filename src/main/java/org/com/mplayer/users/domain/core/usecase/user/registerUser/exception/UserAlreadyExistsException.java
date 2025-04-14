package org.com.mplayer.users.domain.core.usecase.user.registerUser.exception;

import org.com.mplayer.global.ConflictException;

public class UserAlreadyExistsException extends ConflictException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
