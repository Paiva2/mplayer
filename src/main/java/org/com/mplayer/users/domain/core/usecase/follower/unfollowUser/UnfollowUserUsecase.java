package org.com.mplayer.users.domain.core.usecase.follower.unfollowUser;

import lombok.AllArgsConstructor;
import org.com.mplayer.users.domain.core.entity.Follower;
import org.com.mplayer.users.domain.core.entity.User;
import org.com.mplayer.users.domain.core.usecase.common.exception.FollowerNotFoundException;
import org.com.mplayer.users.domain.core.usecase.common.exception.UserDisabledException;
import org.com.mplayer.users.domain.core.usecase.common.exception.UserNotFoundException;
import org.com.mplayer.users.domain.ports.in.usecase.UnfollowUserUsecasePort;
import org.com.mplayer.users.domain.ports.out.data.FollowerDataProviderPort;
import org.com.mplayer.users.domain.ports.out.data.UserDataProviderPort;
import org.com.mplayer.users.infra.annotation.Usecase;

@Usecase
@AllArgsConstructor
public class UnfollowUserUsecase implements UnfollowUserUsecasePort {
    private final UserDataProviderPort userDataProviderPort;
    private final FollowerDataProviderPort followerDataProviderPort;

    @Override
    public void execute(Long userId, Long userFollowedId) {
        User user = findUser(userId);

        if (!user.getEnabled()) {
            throw new UserDisabledException();
        }

        User userFollowed = findUser(userFollowedId);

        Follower follower = findFollow(user, userFollowed);
        removeFollower(follower);
    }

    private User findUser(Long userId) {
        return userDataProviderPort.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    private Follower findFollow(User user, User userFollowed) {
        return followerDataProviderPort.findFollowerByUserAndUserFollowed(user.getId(), userFollowed.getId()).orElseThrow(FollowerNotFoundException::new);
    }

    private void removeFollower(Follower follower) {
        followerDataProviderPort.remove(follower);
    }
}
