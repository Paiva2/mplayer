package org.com.mplayer.player.domain.ports.in.usecase;

import org.com.mplayer.player.domain.ports.in.external.EditPlaylistInputPort;

public interface EditPlaylistUsecasePort {
    void execute(Long playlistId, EditPlaylistInputPort input);
}
