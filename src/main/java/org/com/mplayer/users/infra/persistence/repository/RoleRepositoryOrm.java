package org.com.mplayer.users.infra.persistence.repository;

import org.com.mplayer.users.domain.core.enums.ERole;
import org.com.mplayer.users.infra.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepositoryOrm extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(ERole name);
}
