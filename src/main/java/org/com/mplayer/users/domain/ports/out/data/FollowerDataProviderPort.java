package org.com.mplayer.users.domain.ports.out.data;

import org.com.mplayer.users.domain.core.entity.Follower;
import org.com.mplayer.users.domain.ports.out.utils.PageData;

import java.util.Optional;

public interface FollowerDataProviderPort {
    Optional<Follower> findFollowerByUserAndUserFollowed(Long userId, Long userFollowedId);

    Follower persist(Follower follower);

    void remove(Follower follower);

    PageData<Follower> findUserFollowingList(Long userId, int page, int size, String followedName);

    PageData<Follower> findUserFollowersList(Long userId, int page, int size, String followerName);
}
