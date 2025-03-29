package org.com.mplayer.player.infra.persistence.repository;

import org.com.mplayer.player.infra.persistence.entity.MusicQueueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MusicQueueRepositoryOrm extends JpaRepository<MusicQueueEntity, MusicQueueEntity.KeyId> {
    @Query("select muq from MusicQueueEntity muq " +
        "join fetch muq.music mus " +
        "left join fetch mus.collection col " +
        "where muq.queue.id = :queueId")
    List<MusicQueueEntity> findAllByQueueId(@Param("queueId") Long queueId);

    @Query(value = "insert into player.tb_music_queue (muq_position, muq_queue_id, muq_music_id) " +
        "values ((select (case when max(muq2.muq_position) is null then 1 else max(muq2.muq_position) + 1 end) from player.tb_music_queue muq2 where muq2.muq_queue_id = :queueId), :queueId, :musicId) " +
        "returning *", nativeQuery = true)
    MusicQueueEntity insertLastPosition(@Param("queueId") Long queueId, @Param("musicId") Long musicId);

    List<MusicQueueEntity> findAllByQueueIdAndMusicId(@Param("queueId") Long queueId, @Param("musicId") Long musicId);

    @Modifying
    @Query(value = "delete from player.tb_music_queue where muq_queue_id = :queueId and muq_music_id = :musicId and muq_position = :position", nativeQuery = true)
    void deleteByQueueAndMusicAndPosition(@Param("queueId") Long queueId, @Param("musicId") Long musicId, @Param("position") Integer position);

    @Modifying
    @Query(value = "update player.tb_music_queue set muq_position = muq_position - 1 where muq_queue_id = :queueId and muq_position > :position", nativeQuery = true)
    void updatePositionsDecreasingHigherThanPositionAndQueueId(@Param("queueId") Long queueId, @Param("position") Integer position);
}
