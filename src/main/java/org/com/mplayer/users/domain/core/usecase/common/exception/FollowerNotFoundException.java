package org.com.mplayer.users.domain.core.usecase.common.exception;

import org.com.mplayer.NotFoundException;

public class FollowerNotFoundException extends NotFoundException {
    public FollowerNotFoundException(String message) {
        super(message);
    }

    public FollowerNotFoundException() {
        super("Follower not found!");
    }
}
