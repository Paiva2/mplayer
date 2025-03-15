package org.com.mplayer.users.domain.ports.out.data;

import org.com.mplayer.users.domain.core.entity.User;

import java.util.Optional;

public interface UserDataProviderPort {
    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    User persist(User user);
}
