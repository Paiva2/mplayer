package org.com.mplayer.player.infra.adapter.data.exception;

import org.com.mplayer.BadRequestException;

public class ExternalErrorException extends BadRequestException {
    public ExternalErrorException(String message) {
        super(message);
    }
}
