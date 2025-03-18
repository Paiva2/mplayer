package org.com.mplayer.users.domain.ports.in.usecase;

import org.com.mplayer.users.domain.ports.out.usecase.ListFollowingOutputPort;
import org.com.mplayer.users.domain.ports.out.utils.PageData;

public interface ListFollowingUsecasePort {
    PageData<ListFollowingOutputPort> execute(Long userId, int page, int size, String followedName);
}
