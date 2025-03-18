package org.com.mplayer.users.domain.core.usecase.common.exception;

import org.com.mplayer.users.domain.core.usecase.common.exception.core.ForbiddenException;

public class UserDisabledException extends ForbiddenException {
    private final static String DEFAULT = "User disabled!";

    public UserDisabledException() {
        super(DEFAULT);
    }

    public UserDisabledException(String message) {
        super(message);
    }
}
