package org.com.mplayer.player.infra.persistence.repository;

import org.com.mplayer.player.domain.core.enums.EFileType;
import org.com.mplayer.player.infra.persistence.entity.MusicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MusicRepositoryOrm extends JpaRepository<MusicEntity, Long> {
    @Query("select mus from MusicEntity mus " +
        "where mus.externalUserId = :externalUserId " +
        "and mus.artist = :artist " +
        "and mus.title = :track " +
        "and mus.fileType = :contentType")
    Optional<MusicEntity> findByUserIdAndArtistAndTrack(@Param("externalUserId") String externalUserId, @Param("artist") String artist, @Param("track") String track, @Param("contentType") EFileType contentType);

    @Query("select mus from MusicEntity mus " +
        "left join fetch mus.collection col " +
        "join fetch mus.lyric lyr " +
        "where mus.id = :id")
    Optional<MusicEntity> findByIdWithDeps(@Param("id") Long id);
}
