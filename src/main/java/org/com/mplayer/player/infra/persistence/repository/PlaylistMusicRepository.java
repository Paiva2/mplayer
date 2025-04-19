package org.com.mplayer.player.infra.persistence.repository;

import org.com.mplayer.player.infra.persistence.entity.PlaylistMusicEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaylistMusicRepository extends JpaRepository<PlaylistMusicEntity, PlaylistMusicEntity.KeyId> {
    Optional<PlaylistMusicEntity> findByPlaylistIdAndMusicId(Long playlistId, Long musicId);

    @Query(value = "select coalesce((select plm.plm_position from player.tb_playlist_music plm " +
        "where plm.plm_playlist_id = :playlistId " +
        "order by plm.plm_position " +
        "desc limit 1), 0)", nativeQuery = true)
    Integer findLastPositionMusicByPlaylistId(@Param("playlistId") Long playlistId);

    @Query("select pm from PlaylistMusicEntity pm " +
        "join fetch pm.music mu " +
        "join fetch pm.playlist ply " +
        "left join fetch mu.collection col " +
        "where ply.id = :playlistId")
    Page<PlaylistMusicEntity> findAllByPlaylistId(Long playlistId, Pageable pageable);

    @Query("select pm from PlaylistMusicEntity pm " +
        "join fetch pm.music mu " +
        "join fetch pm.playlist ply " +
        "left join fetch mu.collection col " +
        "where ply.id = :playlistId")
    List<PlaylistMusicEntity> findAllByPlaylistId(Long playlistId);

    @Modifying
    void deleteAllByPlaylistId(Long playlistId);

    @Modifying
    void deleteByPlaylistIdAndMusicId(Long playlistId, Long musicId);

    @Modifying
    @Query("update PlaylistMusicEntity plm " +
        "set plm.position = plm.position - 1 " +
        "where plm.position > :position " +
        "and plm.playlist.id = :playlistId")
    void updatePositionsDecreasingByPlaylistIdAndPosition(Long playlistId, Integer position);
}
