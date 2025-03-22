package org.com.mplayer.player.domain.ports.out.data;

import org.com.mplayer.player.domain.core.entity.Queue;

public interface QueueDataProviderPort {
    Queue persist(Queue queue);
}
