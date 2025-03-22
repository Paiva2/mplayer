package org.com.mplayer.player.domain.core.usecase.common.exception;

import org.com.mplayer.NotFoundException;

public class MusicNotFoundException extends NotFoundException {
    private final static String DEFAULT = "Music not found!";

    public MusicNotFoundException() {
        super(DEFAULT);
    }
}
