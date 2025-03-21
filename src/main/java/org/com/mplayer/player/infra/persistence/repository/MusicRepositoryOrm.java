package org.com.mplayer.player.infra.persistence.repository;

import org.com.mplayer.player.domain.core.enums.EFileType;
import org.com.mplayer.player.infra.persistence.entity.MusicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MusicRepositoryOrm extends JpaRepository<MusicEntity, Long> {
    @Query("select mus from MusicEntity mus " +
        "where mus.externalUserId = :externalUserId " +
        "and mus.artist = :artist " +
        "and mus.title = :track " +
        "and mus.fileType = :contentType")
    Optional<MusicEntity> findByUserIdAndArtistAndTrack(String externalUserId, String artist, String track, EFileType contentType);
}
