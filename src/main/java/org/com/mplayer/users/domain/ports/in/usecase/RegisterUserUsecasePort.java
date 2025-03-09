package org.com.mplayer.users.domain.ports.in.usecase;

import org.com.mplayer.users.application.dto.user.RegisterUserDTO;


public interface RegisterUserUsecasePort {
    void execute(RegisterUserDTO dto);
}
