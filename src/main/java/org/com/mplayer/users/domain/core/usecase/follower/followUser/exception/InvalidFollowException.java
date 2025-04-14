package org.com.mplayer.users.domain.core.usecase.follower.followUser.exception;

import org.com.mplayer.global.BadRequestException;

public class InvalidFollowException extends BadRequestException {
    public InvalidFollowException(String message) {
        super(message);
    }
}
