package org.com.mplayer.users.infra.mapper;

import org.com.mplayer.users.domain.core.entity.Role;
import org.com.mplayer.users.domain.core.entity.User;
import org.com.mplayer.users.domain.core.entity.UserRole;
import org.com.mplayer.users.infra.interfaces.Mapper;
import org.com.mplayer.users.infra.persistence.entity.RoleEntity;
import org.com.mplayer.users.infra.persistence.entity.UserEntity;
import org.com.mplayer.users.infra.persistence.entity.UserRoleEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserRoleMapper implements Mapper<UserRole, UserRoleEntity> {
    @Override
    public UserRole toDomain(UserRoleEntity persistenceEntity) {
        if (persistenceEntity == null) return null;

        UserRole userRole = new UserRole();
        map(persistenceEntity, userRole);

        if (persistenceEntity.getRole() != null) {
            Role role = new Role();
            map(persistenceEntity.getRole(), role);
            userRole.setRole(role);
        }

        if (persistenceEntity.getUser() != null) {
            User user = new User();
            map(persistenceEntity.getUser(), user);
            userRole.setUser(user);
        }

        if (persistenceEntity.getId() != null) {
            UserRole.KeyId id = new UserRole.KeyId();
            map(persistenceEntity.getId(), id);
            userRole.setId(id);
        }

        return userRole;
    }

    @Override
    public UserRoleEntity toPersistence(UserRole domainEntity) {
        if (domainEntity == null) return null;

        UserRoleEntity userRole = new UserRoleEntity();
        map(domainEntity, userRole);

        if (domainEntity.getRole() != null) {
            RoleEntity role = new RoleEntity();
            map(domainEntity.getRole(), role);
            userRole.setRole(role);
        }

        if (domainEntity.getUser() != null) {
            UserEntity user = new UserEntity();
            map(domainEntity.getUser(), user);
            userRole.setUser(user);
        }

        if (domainEntity.getId() != null) {
            UserRoleEntity.KeyId id = new UserRoleEntity.KeyId();
            map(domainEntity.getId(), id);
            userRole.setId(id);
        }

        return userRole;
    }

    @Override
    public void map(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
