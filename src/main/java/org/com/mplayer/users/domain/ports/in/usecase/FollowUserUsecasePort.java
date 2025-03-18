package org.com.mplayer.users.domain.ports.in.usecase;

public interface FollowUserUsecasePort {
    void execute(Long userId, Long userFollowedId);
}
