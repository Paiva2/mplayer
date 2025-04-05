package org.com.mplayer.player.domain.ports.in.usecase;

public interface ChangeMusicQueuePositionUsecasePort {
    void execute(Long musicId, Integer currentPosition, Integer newPosition);
}
