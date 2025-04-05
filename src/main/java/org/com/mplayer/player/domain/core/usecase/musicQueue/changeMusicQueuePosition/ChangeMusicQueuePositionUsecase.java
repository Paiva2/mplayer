package org.com.mplayer.player.domain.core.usecase.musicQueue.changeMusicQueuePosition;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.MusicQueue;
import org.com.mplayer.player.domain.core.entity.Queue;
import org.com.mplayer.player.domain.core.usecase.common.exception.QueueNotFoundException;
import org.com.mplayer.player.domain.ports.in.usecase.ChangeMusicQueuePositionUsecasePort;
import org.com.mplayer.player.domain.ports.out.data.MusicQueueDataProviderPort;
import org.com.mplayer.player.domain.ports.out.data.QueueDataProviderPort;
import org.com.mplayer.player.domain.ports.out.external.UserExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileDTO;
import org.com.mplayer.player.infra.annotations.Usecase;

import java.util.List;

@Usecase
@AllArgsConstructor
public class ChangeMusicQueuePositionUsecase implements ChangeMusicQueuePositionUsecasePort {
    private final UserExternalIntegrationPort userExternalIntegrationPort;
    private final QueueDataProviderPort queueDataProviderPort;
    private final MusicQueueDataProviderPort musicQueueDataProviderPort;

    @Override
    @Transactional
    public void execute(Long musicId, Integer currentPosition, Integer newPosition) {
        if (currentPosition.equals(newPosition)) return;

        FindUserExternalProfileDTO user = findUser();
        Queue userQueue = findQueue(user.getId().toString());
        List<MusicQueue> musicsQueue = findAllMusicsQueue(userQueue);

        if (newPosition > musicsQueue.size()) {
            newPosition = musicsQueue.get(musicsQueue.size() - 1).getPosition();
        }

        updateMusicQueuePosition(musicsQueue, musicId, currentPosition, newPosition);
    }

    private FindUserExternalProfileDTO findUser() {
        return userExternalIntegrationPort.findByExternalId();
    }

    private Queue findQueue(String id) {
        return queueDataProviderPort.findByUser(id).orElseThrow(QueueNotFoundException::new);
    }

    private List<MusicQueue> findAllMusicsQueue(Queue queue) {
        return musicQueueDataProviderPort.getAllByQueue(queue.getId());
    }

    private void updateMusicQueuePosition(List<MusicQueue> musicsQueue, Long musicId, Integer currentPosition, Integer newPosition) {
        for (MusicQueue musicQueue : musicsQueue) {
            if (musicQueue.getId().getMusicId().equals(musicId) && musicQueue.getPosition().equals(currentPosition)) {
                musicQueue.setPosition(newPosition);
            } else if (currentPosition > newPosition && musicQueue.getPosition() >= newPosition && musicQueue.getPosition() <= currentPosition) {
                musicQueue.setPosition(musicQueue.getPosition() + 1);
            } else if (newPosition > currentPosition && musicQueue.getPosition() >= currentPosition && musicQueue.getPosition() <= newPosition) {
                musicQueue.setPosition(musicQueue.getPosition() - 1);
            }
        }

        musicQueueDataProviderPort.insertAll(musicsQueue);
    }
}
