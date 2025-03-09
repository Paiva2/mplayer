package org.com.mplayer.users.domain.core.usecase.common;

import org.com.mplayer.users.domain.core.usecase.common.exception.core.NotFoundException;

public class RoleNotFoundException extends NotFoundException {
    private final static String DEFAULT = "Role not found!";

    public RoleNotFoundException() {
        super(DEFAULT);
    }
}
