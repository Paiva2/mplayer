package org.com.mplayer.player.infra.persistence.repository;

import org.com.mplayer.player.infra.persistence.entity.CollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CollectionRepositoryOrm extends JpaRepository<CollectionEntity, Long> {
    @Query("select col from CollectionEntity col " +
        "where col.externalUserId = :externalUserId " +
        "and col.title = :albumName " +
        "and col.artist = :artist")
    Optional<CollectionEntity> findByUserExternalIdAndAlbumNameAndArtist(@Param("externalUserId") String externalUserId, @Param("albumName") String albumName, @Param("artist") String artist);
}
