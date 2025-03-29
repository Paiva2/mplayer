package org.com.mplayer.player.domain.core.usecase.queue.listQueueMusics;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.MusicQueue;
import org.com.mplayer.player.domain.core.entity.Queue;
import org.com.mplayer.player.domain.core.usecase.common.exception.QueueNotFoundException;
import org.com.mplayer.player.domain.ports.in.usecase.ListQueueMusicsUsecasePort;
import org.com.mplayer.player.domain.ports.out.data.MusicQueueDataProviderPort;
import org.com.mplayer.player.domain.ports.out.data.QueueDataProviderPort;
import org.com.mplayer.player.domain.ports.out.external.UserExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileDTO;
import org.com.mplayer.player.domain.ports.out.external.dto.ListQueueMusicsDTO;
import org.com.mplayer.player.infra.annotations.Usecase;

import java.util.List;

@Usecase
@AllArgsConstructor
public class ListQueueMusicsUsecase implements ListQueueMusicsUsecasePort {
    private final UserExternalIntegrationPort userExternalIntegrationPort;
    private final QueueDataProviderPort queueDataProviderPort;
    private final MusicQueueDataProviderPort musicQueueDataProviderPort;

    @Override
    public ListQueueMusicsDTO execute() {
        FindUserExternalProfileDTO user = findUser();

        Queue queue = findQueue(user.getId().toString());
        List<MusicQueue> musicsQueue = findMusicsQueue(queue.getId());

        return mountOutput(queue.getId(), musicsQueue);
    }

    private FindUserExternalProfileDTO findUser() {
        return userExternalIntegrationPort.findByExternalId();
    }

    private Queue findQueue(String userId) {
        return queueDataProviderPort.findByUser(userId).orElseThrow(QueueNotFoundException::new);
    }

    private List<MusicQueue> findMusicsQueue(Long queueId) {
        return musicQueueDataProviderPort.getAllByQueue(queueId);
    }

    private ListQueueMusicsDTO mountOutput(Long queueId, List<MusicQueue> musicsQueue) {
        return ListQueueMusicsDTO.builder()
            .queueId(queueId)
            .musics(musicsQueue.stream().map(ListQueueMusicsDTO.MusicQueueDTO::new).toList())
            .build();
    }
}
