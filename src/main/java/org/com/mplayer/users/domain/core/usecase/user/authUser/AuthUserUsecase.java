package org.com.mplayer.users.domain.core.usecase.user.authUser;

import lombok.AllArgsConstructor;
import org.com.mplayer.users.application.dto.user.AuthUserDTO;
import org.com.mplayer.users.domain.core.entity.User;
import org.com.mplayer.users.domain.core.usecase.common.exception.UserDisabledException;
import org.com.mplayer.users.domain.core.usecase.common.exception.UserNotFoundException;
import org.com.mplayer.users.domain.core.usecase.user.authUser.exception.InvalidCredentialsException;
import org.com.mplayer.users.domain.ports.in.usecase.AuthUserUsecasePort;
import org.com.mplayer.users.domain.ports.out.data.UserDataProviderPort;
import org.com.mplayer.users.domain.ports.out.usecase.AuthUserOutPort;
import org.com.mplayer.users.domain.ports.out.utils.AuthUtilsPort;
import org.com.mplayer.users.domain.ports.out.utils.PasswordUtilsPort;
import org.com.mplayer.users.infra.annotation.Usecase;

@Usecase
@AllArgsConstructor
public class AuthUserUsecase implements AuthUserUsecasePort {
    private final UserDataProviderPort userDataProviderPort;

    private final PasswordUtilsPort passwordUtilsPort;
    private final AuthUtilsPort authUtilsPort;

    public AuthUserOutPort execute(AuthUserDTO input) {
        User user = findUser(input);

        if (!user.getEnabled()) {
            throw new UserDisabledException();
        }

        checkPassword(input, user);

        return mountOutput(user);
    }

    private User findUser(AuthUserDTO input) {
        return userDataProviderPort.findByEmail(input.getEmail()).orElseThrow(UserNotFoundException::new);
    }

    private void checkPassword(AuthUserDTO input, User user) {
        boolean passwordMatches = passwordUtilsPort.comparePassword(input.getPassword(), user.getPassword());

        if (!passwordMatches) {
            throw new InvalidCredentialsException("Invalid credentials!");
        }
    }

    private String generateAuthentication(User user) {
        return authUtilsPort.generate(user.getId().toString());
    }

    private AuthUserOutPort mountOutput(User user) {
        return AuthUserOutPort.builder()
            .token(generateAuthentication(user))
            .build();
    }
}
