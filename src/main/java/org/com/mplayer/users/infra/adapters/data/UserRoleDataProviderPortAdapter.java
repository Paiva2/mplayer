package org.com.mplayer.users.infra.adapters.data;

import lombok.AllArgsConstructor;
import org.com.mplayer.users.domain.core.entity.UserRole;
import org.com.mplayer.users.domain.ports.out.data.UserRoleDataProviderPort;
import org.com.mplayer.users.infra.mapper.UserRoleMapper;
import org.com.mplayer.users.infra.persistence.entity.UserRoleEntity;
import org.com.mplayer.users.infra.persistence.repository.UserRoleRepositoryOrm;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserRoleDataProviderPortAdapter implements UserRoleDataProviderPort {
    private final UserRoleRepositoryOrm repository;

    private final UserRoleMapper userRoleMapper;

    @Override
    public UserRole persist(UserRole userRole) {
        UserRoleEntity userRoleEntity = repository.save(userRoleMapper.toPersistence(userRole));
        return userRoleMapper.toDomain(userRoleEntity);
    }
}
