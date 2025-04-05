package org.com.mplayer.player.domain.core.usecase.common.exception;

import org.com.mplayer.NotFoundException;

public class MusicQueueNotFoundException extends NotFoundException {
    private final static String DEFAULT = "Music queue not found!";

    public MusicQueueNotFoundException() {
        super(DEFAULT);
    }
}
