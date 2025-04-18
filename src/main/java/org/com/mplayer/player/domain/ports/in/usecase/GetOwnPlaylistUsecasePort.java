package org.com.mplayer.player.domain.ports.in.usecase;

import org.com.mplayer.player.domain.ports.out.external.dto.GetOwnPlaylistOutputPort;

public interface GetOwnPlaylistUsecasePort {
    GetOwnPlaylistOutputPort execute(Long playlistId, int page, int size);
}
