package org.com.mplayer.users.domain.core.usecase.common.exception;

import org.com.mplayer.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    private final static String DEFAULT = "User not found!";

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException() {
        super(DEFAULT);
    }
}
