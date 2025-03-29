package org.com.mplayer.player.domain.ports.out.data;

import org.com.mplayer.player.domain.core.entity.Queue;

import java.util.Optional;

public interface QueueDataProviderPort {
    Queue persist(Queue queue);

    Optional<Queue> findByUser(String userId);
}
