package org.com.mplayer.player.infra.persistence.repository;

import org.com.mplayer.player.domain.core.enums.EFileType;
import org.com.mplayer.player.infra.persistence.entity.MusicEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        "left join fetch mus.lyric lyr " +
        "where mus.id = :id")
    Optional<MusicEntity> findByIdWithDeps(@Param("id") Long id);

    @Query(value = "select * from player.tb_music mus " +
        "left join player.tb_collection col on col.col_id = mus.mus_collection_id " +
        "left join player.tb_lyric lyr on lyr.lyr_music_id = mus.mus_id " +
        "where mus.mus_external_user_id = :externalUserId " +
        "and (:title is null or mus_title ilike '%' || :title || '%') " +
        "and (:fileType is null or mus_file_type = :fileType) " +
        "and (:artist is null or mus_artist ilike '%' || :artist || '%')", nativeQuery = true)
    Page<MusicEntity> findAllByExternalUserFiltering(@Param("externalUserId") String externalUserId, @Param("title") String title, @Param("fileType") String fileType, @Param("artist") String artist, Pageable pageable);
}
