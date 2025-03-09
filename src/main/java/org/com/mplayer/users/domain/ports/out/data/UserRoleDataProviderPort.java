package org.com.mplayer.users.domain.ports.out.data;

import org.com.mplayer.users.domain.core.entity.UserRole;

public interface UserRoleDataProviderPort {
    UserRole persist(UserRole userRole);
}
