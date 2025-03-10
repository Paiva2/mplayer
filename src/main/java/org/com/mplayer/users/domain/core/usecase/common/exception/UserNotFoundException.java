package org.com.mplayer.users.domain.core.usecase.common.exception;

import org.com.mplayer.users.domain.core.usecase.common.exception.core.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
