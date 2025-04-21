package org.com.mplayer.player.domain.ports.out.data;

import org.com.mplayer.player.domain.core.entity.Collection;
import org.com.mplayer.player.domain.ports.out.utils.PageData;

import java.util.Optional;

public interface CollectionDataProviderPort {
    Optional<Collection> findCollectionByUserAndAlbumName(String externalUserId, String albumName, String artist);

    PageData<Collection> findByExternalUserId(String externalUserId, int page, int size);

    Optional<Collection> findByIdAndExternalUserId(String externalUserId, Long collectionId);

    Collection persistCollection(Collection collection);

    Optional<Collection> findById(Long id);

    void remove(Collection collection);
}
