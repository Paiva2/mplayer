package org.com.mplayer.users.domain.core.usecase.user.registerUser.exception;

import org.com.mplayer.ConflictException;

public class UserAlreadyExistsException extends ConflictException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
