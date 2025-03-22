package org.com.mplayer.player.infra.persistence.repository;

import org.com.mplayer.player.infra.persistence.entity.LyricEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LyricRepositoryOrm extends JpaRepository<LyricEntity, Long> {
    @Query("select lyr from LyricEntity lyr " +
        "join fetch lyr.music mus " +
        "where mus.artist = :artist " +
        "and mus.title = :title")
    Optional<LyricEntity> findByArtistAndMusicTitle(@Param("artist") String artist, @Param("title") String title);

    @Modifying
    void deleteByMusicId(Long musicId);
}
