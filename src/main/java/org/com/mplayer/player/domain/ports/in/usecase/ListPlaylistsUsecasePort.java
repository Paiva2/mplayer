package org.com.mplayer.player.domain.ports.in.usecase;

import org.com.mplayer.player.domain.ports.out.external.dto.PlaylistOutputPort;
import org.com.mplayer.player.domain.ports.out.utils.PageData;

public interface ListPlaylistsUsecasePort {
    PageData<PlaylistOutputPort> execute(int page, int size, String name, String direction);
}
