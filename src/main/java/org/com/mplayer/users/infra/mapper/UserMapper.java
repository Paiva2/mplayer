package org.com.mplayer.users.infra.mapper;

import org.com.mplayer.users.domain.core.entity.User;
import org.com.mplayer.users.infra.interfaces.Mapper;
import org.com.mplayer.users.infra.persistence.entity.UserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<User, UserEntity> {
    @Override
    public User toDomain(UserEntity persistenceEntity) {
        if (persistenceEntity == null) return null;

        User user = new User();
        map(persistenceEntity, user);

        return user;
    }

    @Override
    public UserEntity toPersistence(User domainEntity) {
        if (domainEntity == null) return null;

        UserEntity user = new UserEntity();
        map(domainEntity, user);

        return user;
    }

    @Override
    public void map(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
