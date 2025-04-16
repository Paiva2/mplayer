package org.com.mplayer.player.domain.core.usecase.musicQueue.removeMusicQueue;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.MusicQueue;
import org.com.mplayer.player.domain.core.entity.Queue;
import org.com.mplayer.player.domain.core.usecase.common.exception.MusicQueueNotFoundException;
import org.com.mplayer.player.domain.core.usecase.common.exception.QueueNotFoundException;
import org.com.mplayer.player.domain.ports.in.external.RemoveMusicQueueInputPort;
import org.com.mplayer.player.domain.ports.in.usecase.RemoveMusicQueueUsecasePort;
import org.com.mplayer.player.domain.ports.out.data.MusicQueueDataProviderPort;
import org.com.mplayer.player.domain.ports.out.data.QueueDataProviderPort;
import org.com.mplayer.player.domain.ports.out.external.UserExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileOutputPort;
import org.com.mplayer.player.infra.annotations.Usecase;

@Usecase
@AllArgsConstructor
public class RemoveMusicQueueUsecase implements RemoveMusicQueueUsecasePort {
    private final UserExternalIntegrationPort userExternalIntegrationPort;
    private final QueueDataProviderPort queueDataProviderPort;
    private final MusicQueueDataProviderPort musicQueueDataProviderPort;

    @Override
    @Transactional
    public void execute(RemoveMusicQueueInputPort input) {
        FindUserExternalProfileOutputPort user = findUser();

        Queue userQueue = findQueue(user.getId().toString());
        MusicQueue musicQueue = findMusicQueue(userQueue, input);

        removeMusicQueue(musicQueue, userQueue.getId());
    }

    private FindUserExternalProfileOutputPort findUser() {
        return userExternalIntegrationPort.findByExternalId();
    }

    private Queue findQueue(String id) {
        return queueDataProviderPort.findByUser(id).orElseThrow(QueueNotFoundException::new);
    }

    private MusicQueue findMusicQueue(Queue userQueue, RemoveMusicQueueInputPort input) {
        return musicQueueDataProviderPort.findByQueueAndMusicPosition(userQueue.getId(), input.getMusicId(), input.getPosition()).orElseThrow(MusicQueueNotFoundException::new);
    }

    private void removeMusicQueue(MusicQueue musicQueue, Long queueId) {
        musicQueueDataProviderPort.remove(musicQueue);
        reorganizeQueue(queueId, musicQueue.getPosition());
    }

    private void reorganizeQueue(Long queueId, Integer positionRemoved) {
        musicQueueDataProviderPort.updateDecreasePositionsHigher(queueId, positionRemoved);
    }
}
