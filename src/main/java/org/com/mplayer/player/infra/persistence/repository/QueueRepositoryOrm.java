package org.com.mplayer.player.infra.persistence.repository;

import org.com.mplayer.player.infra.persistence.entity.QueueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QueueRepositoryOrm extends JpaRepository<QueueEntity, Long> {
    @Query("select que from QueueEntity que where que.externalUserId = :userId")
    Optional<QueueEntity> findByUserId(@Param("userId") String userId);
}
