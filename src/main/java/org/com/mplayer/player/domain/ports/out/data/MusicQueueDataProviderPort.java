package org.com.mplayer.player.domain.ports.out.data;

import org.com.mplayer.player.domain.core.entity.MusicQueue;

import java.util.List;
import java.util.Optional;

public interface MusicQueueDataProviderPort {
    List<MusicQueue> getAllByQueue(Long queueId);

    Optional<MusicQueue> findByQueueAndMusicPosition(Long queueId, Long musicId, int position);

    MusicQueue insertMusicQueueLastPosition(Long queueId, Long musicId);

    List<MusicQueue> insertAll(List<MusicQueue> musicQueues);

    List<MusicQueue> getAllByQueueAndMusic(Long queueId, Long musicId);

    void remove(MusicQueue musicQueue);

    void updateDecreasePositionsHigher(Long queueId, Integer position);
}
