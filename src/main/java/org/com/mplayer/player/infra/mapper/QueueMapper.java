package org.com.mplayer.player.infra.mapper;

import org.com.mplayer.player.domain.core.entity.Queue;
import org.com.mplayer.player.infra.interfaces.Mapper;
import org.com.mplayer.player.infra.persistence.entity.QueueEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class QueueMapper implements Mapper<Queue, QueueEntity> {
    @Override
    public Queue toDomain(QueueEntity persistenceEntity) {
        if (persistenceEntity == null) return null;
        Queue queue = new Queue();
        map(persistenceEntity, queue);

        return queue;
    }

    @Override
    public QueueEntity toPersistence(Queue domainEntity) {
        if (domainEntity == null) return null;
        QueueEntity queueEntity = new QueueEntity();
        map(domainEntity, queueEntity);

        return queueEntity;
    }

    @Override
    public void map(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
