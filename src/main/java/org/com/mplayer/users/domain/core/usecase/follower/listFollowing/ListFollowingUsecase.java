package org.com.mplayer.users.domain.core.usecase.follower.listFollowing;

import lombok.AllArgsConstructor;
import org.com.mplayer.users.domain.core.entity.Follower;
import org.com.mplayer.users.domain.core.entity.User;
import org.com.mplayer.users.domain.core.usecase.common.exception.UserDisabledException;
import org.com.mplayer.users.domain.core.usecase.common.exception.UserNotFoundException;
import org.com.mplayer.users.domain.ports.in.usecase.ListFollowingUsecasePort;
import org.com.mplayer.users.domain.ports.out.data.FollowerDataProviderPort;
import org.com.mplayer.users.domain.ports.out.data.UserDataProviderPort;
import org.com.mplayer.users.domain.ports.out.usecase.ListFollowingOutputPort;
import org.com.mplayer.users.domain.ports.out.utils.PageData;
import org.com.mplayer.users.infra.annotation.Usecase;

@Usecase
@AllArgsConstructor
public class ListFollowingUsecase implements ListFollowingUsecasePort {
    private final UserDataProviderPort userDataProviderPort;
    private final FollowerDataProviderPort followerDataProviderPort;

    @Override
    public PageData<ListFollowingOutputPort> execute(Long userId, int page, int size, String followedName) {
        User user = findUser(userId);

        if (!user.getEnabled()) {
            throw new UserDisabledException();
        }

        if (page < 1) {
            page = 1;
        }

        if (size < 15) {
            size = 15;
        } else if (size > 50) {
            size = 50;
        }

        PageData<Follower> followingList = findFollowingList(user, page, size, followedName);

        return mountOutput(followingList);
    }

    private User findUser(Long userId) {
        return userDataProviderPort.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    private PageData<Follower> findFollowingList(User user, int page, int size, String followedName) {
        return followerDataProviderPort.findUserFollowingList(user.getId(), page, size, followedName);
    }

    private PageData<ListFollowingOutputPort> mountOutput(PageData<Follower> followingList) {
        return new PageData<>(
            followingList.page(),
            followingList.size(),
            followingList.totalPages(),
            followingList.totalItems(),
            followingList.isLast(),
            followingList.list().stream().map(ListFollowingOutputPort::new).toList()
        );
    }
}
