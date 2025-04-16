package org.com.mplayer.player.infra.persistence.repository;

import org.com.mplayer.player.infra.persistence.entity.PlaylistEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PlaylistRepositoryOrm extends JpaRepository<PlaylistEntity, Long> {
    Optional<PlaylistEntity> findByNameAndExternalUserId(String name, String externalUserId);

    Optional<PlaylistEntity> findByIdAndExternalUserId(Long id, String externalUserId);

    @Query(value = "select ply.*, plm.plm_position " +
        "from player.tb_playlist ply " +
        "left join ( " +
        "   select distinct on (plm2.plm_playlist_id) plm2.plm_playlist_id, plm2.plm_position " +
        "   from player.tb_playlist_music plm2 " +
        "   group by plm2.plm_playlist_id, plm2.plm_position" +
        ") as plm " +
        "on ply.ply_id = plm.plm_playlist_id " +
        "where ply.ply_external_user_id = :userId " +
        "and (:name is null or ply.ply_name ilike concat('%', :name, '%'))", nativeQuery = true)
    Page<PlaylistEntity> findAllByUserId(String userId, String name, Pageable pageable);

    @Modifying
    void deleteById(Long id);
}
