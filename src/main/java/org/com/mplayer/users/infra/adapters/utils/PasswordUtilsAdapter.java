package org.com.mplayer.users.infra.adapters.utils;

import lombok.AllArgsConstructor;
import org.com.mplayer.users.domain.ports.out.utils.PasswordUtilsPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PasswordUtilsAdapter implements PasswordUtilsPort {
    private final PasswordEncoder passwordEncoder;

    @Override
    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean comparePassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
