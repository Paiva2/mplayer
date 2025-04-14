package org.com.mplayer.player.domain.core.usecase.common.exception;

import org.com.mplayer.global.ForbiddenException;

public class MusicNotBelongUserException extends ForbiddenException {
    private final static String DEFAULT = "Music does not belong to provided user!";

    public MusicNotBelongUserException() {
        super(DEFAULT);
    }
}
