package org.com.mplayer.player.infra.adapter.data;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Collection;
import org.com.mplayer.player.domain.ports.out.data.CollectionDataProviderPort;
import org.com.mplayer.player.infra.mapper.CollectionMapper;
import org.com.mplayer.player.infra.persistence.entity.CollectionEntity;
import org.com.mplayer.player.infra.persistence.repository.CollectionRepositoryOrm;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class CollectionDataProviderAdapter implements CollectionDataProviderPort {
    private final CollectionRepositoryOrm repository;

    private final CollectionMapper mapper;

    @Override
    public Optional<Collection> findCollectionByUserAndAlbumName(String externalUserId, String albumName, String artist) {
        Optional<CollectionEntity> collection = repository.findByUserExternalIdAndAlbumNameAndArtist(externalUserId, albumName, artist);
        if (collection.isEmpty()) return Optional.empty();

        return Optional.of(mapper.toDomain(collection.get()));
    }

    @Override
    public Collection persistCollection(Collection collection) {
        CollectionEntity newCollection = repository.save(mapper.toPersistence(collection));

        return mapper.toDomain(newCollection);
    }

    @Override
    public Optional<Collection> findById(Long id) {
        Optional<CollectionEntity> collection = repository.findById(id);
        if (collection.isEmpty()) return Optional.empty();
        return Optional.of(mapper.toDomain(collection.get()));
    }

    @Override
    public void remove(Collection collection) {
        repository.delete(mapper.toPersistence(collection));
    }
}
