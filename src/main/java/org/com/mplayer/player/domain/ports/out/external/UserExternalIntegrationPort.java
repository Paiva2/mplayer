package org.com.mplayer.player.domain.ports.out.external;

import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileDTO;

public interface UserExternalIntegrationPort {
    FindUserExternalProfileDTO findByExternalId();
}
