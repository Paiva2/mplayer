package org.com.mplayer.users.infra.adapters.data;

import lombok.AllArgsConstructor;
import org.com.mplayer.users.domain.core.entity.Role;
import org.com.mplayer.users.domain.core.enums.ERole;
import org.com.mplayer.users.domain.ports.out.data.RoleDataProviderPort;
import org.com.mplayer.users.infra.mapper.RoleMapper;
import org.com.mplayer.users.infra.persistence.entity.RoleEntity;
import org.com.mplayer.users.infra.persistence.repository.RoleRepositoryOrm;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class RoleDataProviderAdapter implements RoleDataProviderPort {
    private final RoleRepositoryOrm repository;

    private final RoleMapper roleMapper;

    @Override
    public Optional<Role> findByName(ERole name) {
        Optional<RoleEntity> role = repository.findByName(name);
        if (role.isEmpty()) return Optional.empty();
        return Optional.of(roleMapper.toDomain(role.get()));
    }
}
