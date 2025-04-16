package org.com.mplayer.player.infra.persistence.repository;

import org.com.mplayer.player.infra.persistence.entity.PlaylistMusicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlaylistMusicRepository extends JpaRepository<PlaylistMusicEntity, PlaylistMusicEntity.KeyId> {
    Optional<PlaylistMusicEntity> findByPlaylistIdAndMusicId(Long playlistId, Long musicId);

    @Query(value = "select coalesce((select plm.plm_position from player.tb_playlist_music plm " +
        "where plm.plm_playlist_id = :playlistId " +
        "order by plm.plm_position " +
        "desc limit 1), 0)", nativeQuery = true)
    Integer findLastPositionMusicByPlaylistId(@Param("playlistId") Long playlistId);

    @Modifying
    void deleteAllByPlaylistId(Long playlistId);
}
