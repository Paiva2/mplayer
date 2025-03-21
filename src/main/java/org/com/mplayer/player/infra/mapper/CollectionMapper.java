package org.com.mplayer.player.infra.mapper;


import org.com.mplayer.player.domain.core.entity.Collection;
import org.com.mplayer.player.infra.interfaces.Mapper;
import org.com.mplayer.player.infra.persistence.entity.CollectionEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CollectionMapper implements Mapper<Collection, CollectionEntity> {
    @Override
    public Collection toDomain(CollectionEntity persistenceEntity) {
        if (persistenceEntity == null) return null;

        Collection collection = new Collection();
        map(persistenceEntity, collection);

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
