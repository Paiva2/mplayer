package org.com.mplayer.users.domain.core.usecase.user.forgotPassword;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.com.mplayer.users.application.dto.user.ForgotPasswordDTO;
import org.com.mplayer.users.domain.core.entity.User;
import org.com.mplayer.users.domain.core.usecase.common.exception.UserDisabledException;
import org.com.mplayer.users.domain.core.usecase.common.exception.UserNotFoundException;
import org.com.mplayer.users.domain.ports.in.usecase.ForgotPasswordUsecasePort;
import org.com.mplayer.users.domain.ports.out.data.UserDataProviderPort;
import org.com.mplayer.users.domain.ports.out.utils.EmailUtilsPort;
import org.com.mplayer.users.domain.ports.out.utils.PasswordUtilsPort;
import org.com.mplayer.users.infra.annotation.Usecase;

import java.text.MessageFormat;
import java.util.Optional;

import static org.com.mplayer.users.domain.core.usecase.common.constants.EmailConstants.ForgotPassword.FORGOT_PASSWORD_EMAIL_TEMPLATE;
import static org.com.mplayer.users.domain.core.usecase.common.constants.EmailConstants.ForgotPassword.FORGOT_PASSWORD_EMAIL_TITLE;

@Usecase
@AllArgsConstructor
public class ForgotPasswordUsecase implements ForgotPasswordUsecasePort {
    private final UserDataProviderPort userDataProviderPort;

    private final PasswordUtilsPort passwordUtilsPort;
    private final EmailUtilsPort emailUtilsPort;

    @Transactional
    public void execute(ForgotPasswordDTO dto) {
        User user = findUser(dto.getEmail());

        if (!user.getEnabled()) {
            throw new UserDisabledException();
        }

        generateRandomPassword(user);
    }

    private User findUser(String email) {
        Optional<User> user = userDataProviderPort.findByEmail(email);

        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found!");
        }

        return user.get();
    }

    private void generateRandomPassword(User user) {
        String rawRandomPassword = passwordUtilsPort.genRandomPassword();
        user.setPassword(passwordUtilsPort.hashPassword(rawRandomPassword));

        userDataProviderPort.persist(user);

        sendNewPassword(user, rawRandomPassword);
    }

    private void sendNewPassword(User user, String rawRandomPassword) {
        String content = MessageFormat.format(FORGOT_PASSWORD_EMAIL_TEMPLATE, user.getFirstName(), rawRandomPassword);

        emailUtilsPort.sendSimpleMail(user.getEmail(), FORGOT_PASSWORD_EMAIL_TITLE, content);
    }
}
