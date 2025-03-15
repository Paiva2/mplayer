package org.com.mplayer.users.domain.ports.out.data;

import org.com.mplayer.users.domain.core.entity.UserRole;

import java.util.List;

public interface UserRoleDataProviderPort {
    UserRole persist(UserRole userRole);

    List<UserRole> findByUser(Long userId);
}
