package org.com.mplayer.users.domain.ports.in.usecase;

import org.com.mplayer.users.application.dto.user.ForgotPasswordDTO;

public interface ForgotPasswordUsecasePort {
    void execute(ForgotPasswordDTO dto);
}
