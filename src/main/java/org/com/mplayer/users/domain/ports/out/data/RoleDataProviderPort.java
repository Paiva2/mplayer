package org.com.mplayer.users.domain.ports.out.data;

import org.com.mplayer.users.domain.core.entity.Role;
import org.com.mplayer.users.domain.core.enums.ERole;

import java.util.Optional;

public interface RoleDataProviderPort {
    Optional<Role> findByName(ERole name);
}
