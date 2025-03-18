package org.com.mplayer.users.domain.core.usecase.follower.listFollowers;

import lombok.AllArgsConstructor;
import org.com.mplayer.users.domain.core.entity.Follower;
import org.com.mplayer.users.domain.core.entity.User;
import org.com.mplayer.users.domain.core.usecase.common.exception.UserDisabledException;
import org.com.mplayer.users.domain.core.usecase.common.exception.UserNotFoundException;
import org.com.mplayer.users.domain.ports.in.usecase.ListFollowersUsecasePort;
import org.com.mplayer.users.domain.ports.out.data.FollowerDataProviderPort;
import org.com.mplayer.users.domain.ports.out.data.UserDataProviderPort;
import org.com.mplayer.users.domain.ports.out.usecase.ListFollowersOutputPort;
import org.com.mplayer.users.domain.ports.out.utils.PageData;
import org.com.mplayer.users.infra.annotation.Usecase;

@Usecase
@AllArgsConstructor
public class ListFollowersUsecase implements ListFollowersUsecasePort {
    private final UserDataProviderPort userDataProviderPort;
    private final FollowerDataProviderPort followerDataProviderPort;

    @Override
    public PageData<ListFollowersOutputPort> execute(Long userId, int page, int size, String followerName) {
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

        PageData<Follower> followersList = findFollowersList(user, page, size, followerName);

        return mountOutput(followersList);
    }

    private User findUser(Long userId) {
        return userDataProviderPort.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    private PageData<Follower> findFollowersList(User user, int page, int size, String followedName) {
        return followerDataProviderPort.findUserFollowersList(user.getId(), page, size, followedName);
    }

    private PageData<ListFollowersOutputPort> mountOutput(PageData<Follower> followersList) {
        return new PageData<>(
            followersList.page(),
            followersList.size(),
            followersList.totalPages(),
            followersList.totalItems(),
            followersList.isLast(),
            followersList.list().stream().map(ListFollowersOutputPort::new).toList()
        );
    }
}
