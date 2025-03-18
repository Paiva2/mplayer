package org.com.mplayer.users.infra.mapper;

import org.com.mplayer.users.domain.core.entity.Follower;
import org.com.mplayer.users.domain.core.entity.User;
import org.com.mplayer.users.infra.interfaces.Mapper;
import org.com.mplayer.users.infra.persistence.entity.FollowerEntity;
import org.com.mplayer.users.infra.persistence.entity.UserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class FollowerMapper implements Mapper<Follower, FollowerEntity> {
    @Override
    public Follower toDomain(FollowerEntity persistenceEntity) {
        if (persistenceEntity == null) return null;

        Follower follower = new Follower();
        map(persistenceEntity, follower);

        if (persistenceEntity.getUser() != null) {
            User user = new User();
            map(persistenceEntity.getUser(), user);
            follower.setUser(user);
        }

        if (persistenceEntity.getUserFollowed() != null) {
            User user = new User();
            map(persistenceEntity.getUserFollowed(), user);
            follower.setUserFollowed(user);
        }

        return follower;
    }

    @Override
    public FollowerEntity toPersistence(Follower domainEntity) {
        if (domainEntity == null) return null;

        FollowerEntity follower = new FollowerEntity();
        map(domainEntity, follower);

        if (domainEntity.getUser() != null) {
            UserEntity user = new UserEntity();
            map(domainEntity.getUser(), user);
            follower.setUser(user);
        }

        if (domainEntity.getUserFollowed() != null) {
            UserEntity user = new UserEntity();
            map(domainEntity.getUserFollowed(), user);
            follower.setUserFollowed(user);
        }

        return follower;
    }

    @Override
    public void map(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
