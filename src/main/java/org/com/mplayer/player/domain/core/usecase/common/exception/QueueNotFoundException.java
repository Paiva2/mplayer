package org.com.mplayer.player.domain.core.usecase.common.exception;

import org.com.mplayer.global.NotFoundException;

public class QueueNotFoundException extends NotFoundException {
    private final static String DEFAULT = "Queue not found!";

    public QueueNotFoundException() {
        super(DEFAULT);
    }
}
