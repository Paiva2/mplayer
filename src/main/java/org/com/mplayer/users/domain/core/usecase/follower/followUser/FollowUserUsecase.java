package org.com.mplayer.users.domain.core.usecase.follower.followUser;

import lombok.AllArgsConstructor;
import org.com.mplayer.users.domain.core.entity.Follower;
import org.com.mplayer.users.domain.core.entity.User;
import org.com.mplayer.users.domain.core.usecase.common.exception.UserDisabledException;
import org.com.mplayer.users.domain.core.usecase.common.exception.UserNotFoundException;
import org.com.mplayer.users.domain.core.usecase.follower.followUser.exception.InvalidFollowException;
import org.com.mplayer.users.domain.core.usecase.follower.followUser.exception.UserAlreadyFollowedException;
import org.com.mplayer.users.domain.ports.in.usecase.FollowUserUsecasePort;
import org.com.mplayer.users.domain.ports.out.data.FollowerDataProviderPort;
import org.com.mplayer.users.domain.ports.out.data.UserDataProviderPort;
import org.com.mplayer.users.infra.annotation.Usecase;

import java.util.Optional;

@Usecase
@AllArgsConstructor
public class FollowUserUsecase implements FollowUserUsecasePort {
    private final UserDataProviderPort userDataProviderPort;
    private final FollowerDataProviderPort followerDataProviderPort;

    public void execute(Long userId, Long userFollowedId) {
        if (userId.equals(userFollowedId)) {
            throw new InvalidFollowException("User can't follow himself!");
        }

        User user = findUser(userId);

        if (!user.getEnabled()) {
            throw new UserDisabledException();
        }

        User userFollowed = findUser(userFollowedId);

        if (!userFollowed.getEnabled()) {
            throw new UserDisabledException("User followed disabled!");
        }

        checkUserAlreadyFollowed(user, userFollowed);

        Follower follower = fillFollower(user, userFollowed);
        persistFollower(follower);
    }

    private User findUser(Long userId) {
        return userDataProviderPort.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    private void checkUserAlreadyFollowed(User user, User userFollowed) {
        Optional<Follower> follower = followerDataProviderPort.findFollowerByUserAndUserFollowed(user.getId(), userFollowed.getId());

        if (follower.isPresent()) {
            throw new UserAlreadyFollowedException("User is already followed!");
        }
    }

    private Follower fillFollower(User user, User userFollowed) {
        return Follower.builder()
            .user(user)
            .userFollowed(userFollowed)
            .build();
    }

    private void persistFollower(Follower follower) {
        followerDataProviderPort.persist(follower);
    }
}
