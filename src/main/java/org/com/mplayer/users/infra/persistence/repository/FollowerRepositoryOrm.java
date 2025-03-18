package org.com.mplayer.users.infra.persistence.repository;

import org.com.mplayer.users.infra.persistence.entity.FollowerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FollowerRepositoryOrm extends JpaRepository<FollowerEntity, Long> {
    Optional<FollowerEntity> findByUserIdAndUserFollowedId(Long userId, Long followedId);

    @Query("select f from FollowerEntity f " +
        "where f.user.id = :userId " +
        "and (:followedName is null or f.userFollowed.firstName ilike '%' || cast(:followedName as string) || '%') ")
    Page<FollowerEntity> findFollowerByUserId(@Param("userId") Long userId, @Param("followedName") String followedName, Pageable pageable);

    @Query("select f from FollowerEntity f " +
        "where f.userFollowed.id = :userId " +
        "and (:followerName is null or f.user.firstName " +
        "ilike '%' || cast(:followerName as string) || '%') ")
    Page<FollowerEntity> findFollowersByUserId(@Param("userId") Long userId, @Param("followerName") String followerName, Pageable pageable);
}
