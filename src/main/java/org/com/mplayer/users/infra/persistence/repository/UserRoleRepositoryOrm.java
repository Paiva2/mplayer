package org.com.mplayer.users.infra.persistence.repository;

import org.com.mplayer.users.infra.persistence.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepositoryOrm extends JpaRepository<UserRoleEntity, Long> {
}
