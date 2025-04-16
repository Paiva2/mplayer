package org.com.mplayer.player.domain.ports.in.usecase;

import org.com.mplayer.player.domain.ports.in.external.CreatePlaylistInputPort;

public interface CreatePlaylistUsecasePort {
    void execute(CreatePlaylistInputPort input);
}
