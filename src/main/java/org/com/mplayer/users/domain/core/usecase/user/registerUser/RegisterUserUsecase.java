package org.com.mplayer.users.domain.core.usecase.user.registerUser;

import lombok.AllArgsConstructor;
import org.com.mplayer.users.SendCreateUserQueueEvent;
import org.com.mplayer.users.application.dto.user.RegisterUserDTO;
import org.com.mplayer.users.domain.core.entity.Role;
import org.com.mplayer.users.domain.core.entity.User;
import org.com.mplayer.users.domain.core.entity.UserRole;
import org.com.mplayer.users.domain.core.enums.ERole;
import org.com.mplayer.users.domain.core.usecase.common.exception.InvalidFieldException;
import org.com.mplayer.users.domain.core.usecase.common.exception.RoleNotFoundException;
import org.com.mplayer.users.domain.core.usecase.user.registerUser.exception.UserAlreadyExistsException;
import org.com.mplayer.users.domain.ports.in.usecase.RegisterUserUsecasePort;
import org.com.mplayer.users.domain.ports.out.data.RoleDataProviderPort;
import org.com.mplayer.users.domain.ports.out.data.UserDataProviderPort;
import org.com.mplayer.users.domain.ports.out.data.UserRoleDataProviderPort;
import org.com.mplayer.users.domain.ports.out.event.UserSendApplicationEventPort;
import org.com.mplayer.users.domain.ports.out.utils.EmailUtilsPort;
import org.com.mplayer.users.domain.ports.out.utils.PasswordUtilsPort;
import org.com.mplayer.users.infra.annotation.Usecase;

import java.util.Optional;

@Usecase
@AllArgsConstructor
public class RegisterUserUsecase implements RegisterUserUsecasePort {
    private final UserDataProviderPort userDataProviderPort;
    private final RoleDataProviderPort roleDataProviderPort;
    private final UserRoleDataProviderPort userRoleDataProviderPort;

    private final EmailUtilsPort emailUtilsPort;
    private final PasswordUtilsPort passwordUtilsPort;

    private final UserSendApplicationEventPort userSendApplicationEventPort;

    @Override
    public void execute(RegisterUserDTO dto) {
        checkUserAlreadyExists(dto.getEmail());
        validateEmail(dto.getEmail());
        validatePassword(dto.getPassword());

        User user = fillUser(dto);
        user = persistUser(user);

        Role role = findRoleByName();
        UserRole userRole = fillUserRole(user, role);
        persistUserRole(userRole);

        sendCreateUserQueue(user);
    }

    private void checkUserAlreadyExists(String email) {
        Optional<User> user = userDataProviderPort.findByEmail(email);

        if (user.isPresent()) {
            throw new UserAlreadyExistsException("E-mail already being used!");
        }
    }

    private void validateEmail(String email) {
        if (!emailUtilsPort.validateEmailFormat(email)) {
            throw new InvalidFieldException("Invalid e-mail format!");
        }
    }

    private void validatePassword(String password) {
        if (password.length() < 6) {
            throw new InvalidFieldException("Invalid password. Password must have at least 6 characters!");
        }
    }

    private User fillUser(RegisterUserDTO dto) {
        return User.builder()
            .email(dto.getEmail())
            .firstName(dto.getFirstName())
            .lastName(dto.getLastName())
            .password(hashPassword(dto.getPassword()))
            .enabled(true)
            .build();
    }

    private String hashPassword(String rawPassword) {
        return passwordUtilsPort.hashPassword(rawPassword);
    }

    private User persistUser(User user) {
        return userDataProviderPort.persist(user);
    }

    private Role findRoleByName() {
        return roleDataProviderPort.findByName(ERole.USER).orElseThrow(RoleNotFoundException::new);
    }

    private UserRole fillUserRole(User user, Role role) {
        return UserRole.builder()
            .id(new UserRole.KeyId(user.getId(), role.getId()))
            .role(role)
            .user(user)
            .build();
    }

    private void persistUserRole(UserRole userRole) {
        userRoleDataProviderPort.persist(userRole);
    }

    private void sendCreateUserQueue(User user) {
        userSendApplicationEventPort.send(new SendCreateUserQueueEvent(user.getId().toString()));
    }
}
