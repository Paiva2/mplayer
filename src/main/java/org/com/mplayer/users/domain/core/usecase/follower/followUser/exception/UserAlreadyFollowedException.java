package org.com.mplayer.users.domain.core.usecase.follower.followUser.exception;

import org.com.mplayer.users.domain.core.usecase.common.exception.core.ConflictException;

public class UserAlreadyFollowedException extends ConflictException {
    public UserAlreadyFollowedException(String message) {
        super(message);
    }
}
