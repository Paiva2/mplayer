package org.com.mplayer.users.infra.mapper;

import org.com.mplayer.users.domain.core.entity.Role;
import org.com.mplayer.users.infra.interfaces.Mapper;
import org.com.mplayer.users.infra.persistence.entity.RoleEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper implements Mapper<Role, RoleEntity> {
    @Override
    public Role toDomain(RoleEntity persistenceEntity) {
        if (persistenceEntity == null) return null;

        Role role = new Role();
        map(persistenceEntity, role);

        return role;
    }

    @Override
    public RoleEntity toPersistence(Role domainEntity) {
        if (domainEntity == null) return null;

        RoleEntity role = new RoleEntity();
        map(domainEntity, role);

        return role;
    }

    @Override
    public void map(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
