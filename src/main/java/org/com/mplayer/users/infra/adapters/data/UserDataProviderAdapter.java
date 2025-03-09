package org.com.mplayer.users.infra.adapters.data;

import lombok.AllArgsConstructor;
import org.com.mplayer.users.domain.core.entity.User;
import org.com.mplayer.users.domain.ports.out.data.UserDataProviderPort;
import org.com.mplayer.users.infra.mapper.UserMapper;
import org.com.mplayer.users.infra.persistence.entity.UserEntity;
import org.com.mplayer.users.infra.persistence.repository.UserRepositoryOrm;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserDataProviderAdapter implements UserDataProviderPort {
    private final UserRepositoryOrm repository;

    private final UserMapper userMapper;

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> user = repository.findByEmail(email);
        if (user.isEmpty()) return Optional.empty();
        return Optional.of(userMapper.toDomain(user.get()));
    }

    @Override
    public User persist(User user) {
        UserEntity newUser = repository.save(userMapper.toPersistence(user));
        return userMapper.toDomain(newUser);
    }
}
