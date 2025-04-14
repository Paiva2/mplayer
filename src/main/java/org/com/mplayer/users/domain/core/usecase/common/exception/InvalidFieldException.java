package org.com.mplayer.users.domain.core.usecase.common.exception;

import org.com.mplayer.global.BadRequestException;

public class InvalidFieldException extends BadRequestException {
    public InvalidFieldException(String message) {
        super(message);
    }
}
