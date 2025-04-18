package org.com.mplayer.player.domain.ports.in.usecase;

import org.com.mplayer.player.domain.ports.out.external.dto.GetUserPlaylistOutputPort;

public interface GetUserPlaylistUsecasePort {
    GetUserPlaylistOutputPort execute(String userId, Long playlistId, int page, int size);
}
