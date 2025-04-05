package org.com.mplayer.player.infra.mapper;

import org.com.mplayer.player.domain.core.entity.Collection;
import org.com.mplayer.player.domain.core.entity.Music;
import org.com.mplayer.player.domain.core.entity.MusicQueue;
import org.com.mplayer.player.domain.core.entity.Queue;
import org.com.mplayer.player.infra.interfaces.Mapper;
import org.com.mplayer.player.infra.persistence.entity.CollectionEntity;
import org.com.mplayer.player.infra.persistence.entity.MusicEntity;
import org.com.mplayer.player.infra.persistence.entity.MusicQueueEntity;
import org.com.mplayer.player.infra.persistence.entity.QueueEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class MusicQueueMapper implements Mapper<MusicQueue, MusicQueueEntity> {
    @Override
    public MusicQueue toDomain(MusicQueueEntity persistenceEntity) {
        if (persistenceEntity == null) return null;

        MusicQueue musicQueue = new MusicQueue();
        map(persistenceEntity, musicQueue);

        if (persistenceEntity.getId() != null) {
            MusicQueue.KeyId id = new MusicQueue.KeyId();
            map(persistenceEntity.getId(), id);
            musicQueue.setId(id);
        }

        if (persistenceEntity.getMusic() != null) {
            Music music = new Music();
            map(persistenceEntity.getMusic(), music);
            musicQueue.setMusic(music);

            if (persistenceEntity.getMusic().getCollection() != null) {
                Collection collection = new Collection();
                map(persistenceEntity.getMusic().getCollection(), collection);
                musicQueue.getMusic().setCollection(collection);
            }
        }

        if (persistenceEntity.getQueue() != null) {
            Queue queue = new Queue();
            map(persistenceEntity.getQueue(), queue);
            musicQueue.setQueue(queue);
        }

        return musicQueue;
    }

    @Override
    public MusicQueueEntity toPersistence(MusicQueue domainEntity) {
        if (domainEntity == null) return null;

        MusicQueueEntity musicQueue = new MusicQueueEntity();
        map(domainEntity, musicQueue);

        if (domainEntity.getId() != null) {
            MusicQueueEntity.KeyId id = new MusicQueueEntity.KeyId();
            map(domainEntity.getId(), id);
            musicQueue.setId(id);
        }

        if (domainEntity.getMusic() != null) {
            MusicEntity music = new MusicEntity();
            map(domainEntity.getMusic(), music);
            musicQueue.setMusic(music);

            if (domainEntity.getMusic().getCollection() != null) {
                CollectionEntity collection = new CollectionEntity();
                map(domainEntity.getMusic().getCollection(), collection);
                musicQueue.getMusic().setCollection(collection);
            }
        }

        if (domainEntity.getQueue() != null) {
            QueueEntity queue = new QueueEntity();
            map(domainEntity.getQueue(), queue);
            musicQueue.setQueue(queue);
        }

        return musicQueue;
    }

    @Override
    public void map(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
