package org.com.mplayer.users.domain.ports.in.usecase;

public interface UnfollowUserUsecasePort {
    void execute(Long userId, Long userFollowedId);
}
