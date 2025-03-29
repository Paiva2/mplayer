package org.com.mplayer.player.infra.adapter.data;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.MusicQueue;
import org.com.mplayer.player.domain.ports.out.data.MusicQueueDataProviderPort;
import org.com.mplayer.player.infra.mapper.MusicQueueMapper;
import org.com.mplayer.player.infra.persistence.entity.MusicQueueEntity;
import org.com.mplayer.player.infra.persistence.repository.MusicQueueRepositoryOrm;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class MusicQueueDataProviderAdapter implements MusicQueueDataProviderPort {
    private final MusicQueueRepositoryOrm repository;

    private final MusicQueueMapper mapper;

    @Override
    public List<MusicQueue> getAllByQueue(Long queueId) {
        List<MusicQueueEntity> musicQueue = repository.findAllByQueueId(queueId);
        return musicQueue.stream().map(mapper::toDomain).toList();
    }

    @Override
    public MusicQueue insertMusicQueueLastPosition(Long queueId, Long musicId) {
        MusicQueueEntity newMusicQueue = repository.insertLastPosition(queueId, musicId);
        return mapper.toDomain(newMusicQueue);
    }

    @Override
    public List<MusicQueue> getAllByQueueAndMusic(Long queueId, Long musicId) {
        List<MusicQueueEntity> musicQueueEntities = repository.findAllByQueueIdAndMusicId(queueId, musicId);
        return musicQueueEntities.stream().map(mapper::toDomain).toList();
    }

    @Override
    public void remove(MusicQueue musicQueue) {
        repository.deleteByQueueAndMusicAndPosition(musicQueue.getQueue().getId(), musicQueue.getMusic().getId(), musicQueue.getPosition());
    }

    @Override
    public void updateDecreasePositionsHigher(Long queueId, Integer position) {
        repository.updatePositionsDecreasingHigherThanPositionAndQueueId(queueId, position);
    }
}
