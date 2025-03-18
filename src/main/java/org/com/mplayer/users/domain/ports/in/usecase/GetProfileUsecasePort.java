package org.com.mplayer.users.domain.ports.in.usecase;

import org.com.mplayer.users.domain.ports.out.usecase.GetProfileOutputPort;

public interface GetProfileUsecasePort {
    GetProfileOutputPort execute(Long userId);
}
