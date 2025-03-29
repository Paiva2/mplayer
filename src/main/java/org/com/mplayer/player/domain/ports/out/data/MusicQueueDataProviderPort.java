package org.com.mplayer.player.domain.ports.out.data;

import org.com.mplayer.player.domain.core.entity.MusicQueue;

import java.util.List;

public interface MusicQueueDataProviderPort {
    List<MusicQueue> getAllByQueue(Long queueId);

    MusicQueue insertMusicQueueLastPosition(Long queueId, Long musicId);

    List<MusicQueue> getAllByQueueAndMusic(Long queueId, Long musicId);

    void remove(MusicQueue musicQueue);

    void updateDecreasePositionsHigher(Long queueId, Integer position);
}
