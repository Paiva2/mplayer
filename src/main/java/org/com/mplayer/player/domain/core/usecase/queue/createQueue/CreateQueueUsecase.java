package org.com.mplayer.player.domain.core.usecase.queue.createQueue;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Queue;
import org.com.mplayer.player.domain.ports.in.usecase.CreateQueueUsecasePort;
import org.com.mplayer.player.domain.ports.out.data.QueueDataProviderPort;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CreateQueueUsecase implements CreateQueueUsecasePort {
    private final QueueDataProviderPort queueDataProviderPort;

    @Override
    public void execute(String externalUserId) {
        Queue queue = fillQueue(externalUserId);
        persistQueue(queue);
    }

    private Queue fillQueue(String externalUserId) {
        return Queue.builder()
            .externalUserId(externalUserId)
            .build();
    }

    private void persistQueue(Queue queue) {
        queueDataProviderPort.persist(queue);
    }
}
