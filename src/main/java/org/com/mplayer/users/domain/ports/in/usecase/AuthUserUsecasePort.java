package org.com.mplayer.users.domain.ports.in.usecase;

import org.com.mplayer.users.application.dto.user.AuthUserDTO;
import org.com.mplayer.users.domain.ports.out.usecase.AuthUserOutPort;

public interface AuthUserUsecasePort {
    AuthUserOutPort execute(AuthUserDTO input);
}
