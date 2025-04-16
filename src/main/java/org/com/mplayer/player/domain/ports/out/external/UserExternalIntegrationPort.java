package org.com.mplayer.player.domain.ports.out.external;

import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileOutputPort;

public interface UserExternalIntegrationPort {
    FindUserExternalProfileOutputPort findByExternalId();
}
