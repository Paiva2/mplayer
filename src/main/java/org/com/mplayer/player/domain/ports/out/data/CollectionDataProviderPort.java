package org.com.mplayer.player.domain.ports.out.data;

import org.com.mplayer.player.domain.core.entity.Collection;

import java.util.Optional;

public interface CollectionDataProviderPort {
    Optional<Collection> findCollectionByUserAndAlbumName(String externalUserId, String albumName, String artist);

    Collection persistCollection(Collection collection);
}
