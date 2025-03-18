package org.com.mplayer.users.infra.adapters.data;

import lombok.AllArgsConstructor;
import org.com.mplayer.users.domain.core.entity.Follower;
import org.com.mplayer.users.domain.ports.out.data.FollowerDataProviderPort;
import org.com.mplayer.users.domain.ports.out.utils.PageData;
import org.com.mplayer.users.infra.mapper.FollowerMapper;
import org.com.mplayer.users.infra.persistence.entity.FollowerEntity;
import org.com.mplayer.users.infra.persistence.repository.FollowerRepositoryOrm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class FollowerDataProviderAdapter implements FollowerDataProviderPort {
    private final FollowerRepositoryOrm repository;

    private final FollowerMapper followerMapper;

    @Override
    public Optional<Follower> findFollowerByUserAndUserFollowed(Long userId, Long userFollowedId) {
        Optional<FollowerEntity> follower = repository.findByUserIdAndUserFollowedId(userId, userFollowedId);
        if (follower.isEmpty()) return Optional.empty();

        return Optional.of(followerMapper.toDomain(follower.get()));
    }

    @Override
    public Follower persist(Follower follower) {
        FollowerEntity followerEntity = repository.save(followerMapper.toPersistence(follower));
        return followerMapper.toDomain(followerEntity);
    }

    @Override
    public void remove(Follower follower) {
        repository.delete(followerMapper.toPersistence(follower));
    }

    @Override
    public PageData<Follower> findUserFollowingList(Long userId, int page, int size, String followedName) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "userFollowed.firstName");
        Page<FollowerEntity> followerEntities = repository.findFollowerByUserId(userId, followedName, pageable);

        return new PageData<Follower>(
            followerEntities.getNumber() + 1,
            followerEntities.getSize(),
            followerEntities.getTotalPages(),
            followerEntities.getTotalElements(),
            followerEntities.isLast(),
            followerEntities.stream().map(followerMapper::toDomain).toList()
        );
    }

    @Override
    public PageData<Follower> findUserFollowersList(Long userId, int page, int size, String followerName) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "user.firstName");
        Page<FollowerEntity> followerEntities = repository.findFollowersByUserId(userId, followerName, pageable);

        return new PageData<Follower>(
            followerEntities.getNumber() + 1,
            followerEntities.getSize(),
            followerEntities.getTotalPages(),
            followerEntities.getTotalElements(),
            followerEntities.isLast(),
            followerEntities.stream().map(followerMapper::toDomain).toList()
        );
    }
}
