package org.com.mplayer.users.infra.persistence.repository;

import org.com.mplayer.users.infra.persistence.entity.FollowerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowerRepositoryOrm extends JpaRepository<FollowerEntity, Long> {
}
