package org.com.mplayer.users.domain.ports.in.usecase;

import org.com.mplayer.users.domain.ports.out.usecase.GetProfileOutput;

public interface GetProfileUsecasePort {
    GetProfileOutput execute(Long userId);
}
