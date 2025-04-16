package org.com.mplayer.player.domain.core.usecase.musicQueue.insertMusicQueue;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Music;
import org.com.mplayer.player.domain.core.entity.Queue;
import org.com.mplayer.player.domain.core.usecase.common.exception.MusicNotBelongUserException;
import org.com.mplayer.player.domain.core.usecase.common.exception.MusicNotFoundException;
import org.com.mplayer.player.domain.core.usecase.common.exception.QueueNotFoundException;
import org.com.mplayer.player.domain.ports.in.usecase.InsertMusicQueueUsecasePort;
import org.com.mplayer.player.domain.ports.out.data.MusicDataProviderPort;
import org.com.mplayer.player.domain.ports.out.data.MusicQueueDataProviderPort;
import org.com.mplayer.player.domain.ports.out.data.QueueDataProviderPort;
import org.com.mplayer.player.domain.ports.out.external.UserExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileOutputPort;
import org.com.mplayer.player.infra.annotations.Usecase;

@Usecase
@AllArgsConstructor
public class InsertMusicQueueUsecase implements InsertMusicQueueUsecasePort {
    private final UserExternalIntegrationPort userExternalIntegrationPort;
    private final QueueDataProviderPort queueDataProviderPort;
    private final MusicDataProviderPort musicDataProviderPort;
    private final MusicQueueDataProviderPort musicQueueDataProviderPort;

    @Override
    @Transactional
    public void execute(Long musicId) {
        FindUserExternalProfileOutputPort user = findUser();
        Music music = findMusic(musicId);
        checkUserMusic(user, music);

        Queue userQueue = findQueue(user.getId().toString());

        persistMusicQueue(userQueue, music);
    }

    private FindUserExternalProfileOutputPort findUser() {
        return userExternalIntegrationPort.findByExternalId();
    }

    private Queue findQueue(String id) {
        return queueDataProviderPort.findByUser(id).orElseThrow(QueueNotFoundException::new);
    }

    private Music findMusic(Long musicId) {
        return musicDataProviderPort.findByIdWithDeps(musicId).orElseThrow(MusicNotFoundException::new);
    }

    private void checkUserMusic(FindUserExternalProfileOutputPort user, Music music) {
        if (!user.getId().toString().equals(music.getExternalUserId())) {
            throw new MusicNotBelongUserException();
        }
    }

    private void persistMusicQueue(Queue queue, Music music) {
        musicQueueDataProviderPort.insertMusicQueueLastPosition(queue.getId(), music.getId());
    }
}
