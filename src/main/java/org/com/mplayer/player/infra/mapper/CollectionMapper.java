package org.com.mplayer.player.infra.mapper;


import org.com.mplayer.player.domain.core.entity.Collection;
import org.com.mplayer.player.domain.core.entity.Music;
import org.com.mplayer.player.infra.interfaces.Mapper;
import org.com.mplayer.player.infra.persistence.entity.CollectionEntity;
import org.com.mplayer.player.infra.persistence.entity.MusicEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CollectionMapper implements Mapper<Collection, CollectionEntity> {
    @Override
    public Collection toDomain(CollectionEntity persistenceEntity) {
        if (persistenceEntity == null) return null;

        Collection collection = new Collection();
        map(persistenceEntity, collection);

        if (persistenceEntity.getMusics() != null) {
            List<Music> collectionMusics = new ArrayList<>();

            for (MusicEntity music : persistenceEntity.getMusics()) {
                Music musicDomain = new Music();
                map(music, musicDomain);

                collectionMusics.add(musicDomain);
            }

            collection.setMusics(collectionMusics);
        }

        return collection;
    }

    @Override
    public CollectionEntity toPersistence(Collection domainEntity) {
        if (domainEntity == null) return null;

        CollectionEntity collection = new CollectionEntity();
        map(domainEntity, collection);

        return collection;
    }

    @Override
    public void map(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
