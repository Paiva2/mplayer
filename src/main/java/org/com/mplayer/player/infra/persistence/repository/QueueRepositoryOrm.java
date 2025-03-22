package org.com.mplayer.player.infra.persistence.repository;

import org.com.mplayer.player.infra.persistence.entity.QueueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueRepositoryOrm extends JpaRepository<QueueEntity, Long> {
}
