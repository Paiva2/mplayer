package org.com.mplayer.users.domain.core.usecase.user.getProfile;

import lombok.AllArgsConstructor;
import org.com.mplayer.users.domain.core.entity.User;
import org.com.mplayer.users.domain.core.entity.UserRole;
import org.com.mplayer.users.domain.core.usecase.common.exception.UserDisabledException;
import org.com.mplayer.users.domain.core.usecase.common.exception.UserNotFoundException;
import org.com.mplayer.users.domain.ports.in.usecase.GetProfileUsecasePort;
import org.com.mplayer.users.domain.ports.out.data.UserDataProviderPort;
import org.com.mplayer.users.domain.ports.out.data.UserRoleDataProviderPort;
import org.com.mplayer.users.domain.ports.out.usecase.GetProfileOutputPort;
import org.com.mplayer.users.infra.annotation.Usecase;

import java.util.List;

@Usecase
@AllArgsConstructor
public class GetProfileUsecase implements GetProfileUsecasePort {
    private final UserDataProviderPort userDataProviderPort;
    private final UserRoleDataProviderPort userRoleDataProviderPort;

    @Override
    public GetProfileOutputPort execute(Long userId) {
        User user = findUser(userId);

        if (!user.getEnabled()) {
            throw new UserDisabledException();
        }

        List<UserRole> userRoles = findUserRoles(user);

        return mountOutput(user, userRoles);
    }

    private User findUser(Long userId) {
        return userDataProviderPort.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    private List<UserRole> findUserRoles(User user) {
        return userRoleDataProviderPort.findByUser(user.getId());
    }

    private GetProfileOutputPort mountOutput(User user, List<UserRole> userRoles) {
        return GetProfileOutputPort.builder()
            .id(user.getId())
            .email(user.getEmail())
            .profilePicture(user.getProfilePicture())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .userRoles(userRoles.stream().map(GetProfileOutputPort.UserRoleOutput::new).toList())
            .build();
    }
}
