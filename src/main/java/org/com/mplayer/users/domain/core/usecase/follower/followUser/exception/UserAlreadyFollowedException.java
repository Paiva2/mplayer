package org.com.mplayer.users.domain.core.usecase.follower.followUser.exception;

import org.com.mplayer.global.ConflictException;

public class UserAlreadyFollowedException extends ConflictException {
    public UserAlreadyFollowedException(String message) {
        super(message);
    }
}
