package org.com.mplayer.player.domain.core.usecase.common.exception;

import org.com.mplayer.global.NotFoundException;

public class CollectionNotFoundException extends NotFoundException {
    private final static String DEFAULT = "Collection not found!";

    public CollectionNotFoundException() {
        super(DEFAULT);
    }
}
