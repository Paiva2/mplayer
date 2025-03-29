package org.com.mplayer.player.domain.ports.in.usecase;

import org.com.mplayer.player.domain.ports.out.external.dto.ListQueueMusicsDTO;

public interface ListQueueMusicsUsecasePort {
    ListQueueMusicsDTO execute();
}
