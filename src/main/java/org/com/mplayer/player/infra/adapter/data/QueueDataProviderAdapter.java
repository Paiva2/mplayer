package org.com.mplayer.player.infra.adapter.data;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Queue;
import org.com.mplayer.player.domain.ports.out.data.QueueDataProviderPort;
import org.com.mplayer.player.infra.mapper.QueueMapper;
import org.com.mplayer.player.infra.persistence.entity.QueueEntity;
import org.com.mplayer.player.infra.persistence.repository.QueueRepositoryOrm;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class QueueDataProviderAdapter implements QueueDataProviderPort {
    private final QueueRepositoryOrm repository;

    private final QueueMapper queueMapper;

    @Override
    public Queue persist(Queue queue) {
        QueueEntity newQueue = repository.save(queueMapper.toPersistence(queue));
        return queueMapper.toDomain(newQueue);
    }

    @Override
    public Optional<Queue> findByUser(String userId) {
        Optional<QueueEntity> queue = repository.findByUserId(userId);
        if (queue.isEmpty()) return Optional.empty();
        return Optional.of(queueMapper.toDomain(queue.get()));
    }
}
