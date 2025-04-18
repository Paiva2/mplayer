package org.com.mplayer.player.domain.ports.in.usecase;

import org.com.mplayer.player.domain.ports.out.external.dto.ListUserPlaylistsOutputPort;
import org.com.mplayer.player.domain.ports.out.utils.PageData;

public interface ListUserPlaylistsUsecasePort {
    PageData<ListUserPlaylistsOutputPort> execute(String userId, int page, int size, String name, String direction);
}
