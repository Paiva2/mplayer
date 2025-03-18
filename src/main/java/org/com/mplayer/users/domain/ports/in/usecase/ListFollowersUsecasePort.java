package org.com.mplayer.users.domain.ports.in.usecase;

import org.com.mplayer.users.domain.ports.out.usecase.ListFollowersOutputPort;
import org.com.mplayer.users.domain.ports.out.utils.PageData;

public interface ListFollowersUsecasePort {
    PageData<ListFollowersOutputPort> execute(Long userId, int page, int size, String followerName);
}
