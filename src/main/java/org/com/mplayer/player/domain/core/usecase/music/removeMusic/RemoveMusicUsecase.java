package org.com.mplayer.player.domain.core.usecase.music.removeMusic;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Collection;
import org.com.mplayer.player.domain.core.entity.Music;
import org.com.mplayer.player.domain.core.entity.MusicQueue;
import org.com.mplayer.player.domain.core.entity.Queue;
import org.com.mplayer.player.domain.core.usecase.common.exception.MusicNotBelongUserException;
import org.com.mplayer.player.domain.core.usecase.common.exception.MusicNotFoundException;
import org.com.mplayer.player.domain.core.usecase.common.exception.QueueNotFoundException;
import org.com.mplayer.player.domain.ports.in.usecase.RemoveMusicUsecasePort;
import org.com.mplayer.player.domain.ports.out.data.*;
import org.com.mplayer.player.domain.ports.out.external.FileExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.UserExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RemoveMusicUsecase implements RemoveMusicUsecasePort {
    private final static String FILES_IMAGES_DESTINATION = "mplayer-images";
    private final static String FILES_TRACKS_DESTINATION = "mplayer-tracks";

    private final UserExternalIntegrationPort userExternalIntegrationPort;
    private final FileExternalIntegrationPort fileExternalIntegrationPort;

    private final MusicDataProviderPort musicDataProviderPort;
    private final LyricDataProviderPort lyricDataProviderPort;
    private final CollectionDataProviderPort collectionDataProviderPort;
    private final MusicQueueDataProviderPort musicQueueDataProviderPort;
    private final QueueDataProviderPort queueDataProviderPort;

    @Override
    @Transactional
    public void execute(Long musicId) {
        FindUserExternalProfileDTO user = findUser();

        Music music = findMusic(musicId);

        if (!Long.valueOf(music.getExternalUserId()).equals(user.getId())) {
            throw new MusicNotBelongUserException();
        }

        Queue queue = findQueue(user.getId().toString());

        removeMusicFromQueue(queue, music);
        removeMusicLyric(music);
        removeMusic(music);
        handleMusicCollection(music);

        removeMusicExternal(music);
        removeMusicCoverExternal(music);
    }

    private FindUserExternalProfileDTO findUser() {
        return userExternalIntegrationPort.findByExternalId();
    }

    private Music findMusic(long musicId) {
        return musicDataProviderPort.findByIdWithDeps(musicId).orElseThrow(MusicNotFoundException::new);
    }

    private Queue findQueue(String userId) {
        return queueDataProviderPort.findByUser(userId).orElseThrow(QueueNotFoundException::new);
    }

    private void removeMusicFromQueue(Queue queue, Music music) {
        List<MusicQueue> musicsQueue = musicQueueDataProviderPort.getAllByQueueAndMusic(queue.getId(), music.getId());

        for (MusicQueue musicQueue : musicsQueue) {
            Integer positionRemoved = musicQueue.getPosition();
            
            musicQueueDataProviderPort.remove(musicQueue);
            musicQueueDataProviderPort.updateDecreasePositionsHigher(queue.getId(), positionRemoved);
        }
    }

    private void removeMusicLyric(Music music) {
        lyricDataProviderPort.removeByMusicId(music.getId());
    }

    private void removeMusic(Music music) {
        music.setCollection(null);
        musicDataProviderPort.remove(music);
    }

    private void handleMusicCollection(Music music) {
        if (music.getCollection() == null) return;

        Collection collection = music.getCollection();
        List<Music> musicsFromCollection = collection.getMusics();

        if (musicsFromCollection.size() == 1) {
            removeCollection(collection);
        }
    }

    private void removeCollection(Collection collection) {
        collectionDataProviderPort.remove(collection);
    }

    private void removeMusicExternal(Music music) {
        String externalId = music.getExternalIdentification().concat("_track").concat(".").concat(music.getFileType().getGetTypeLower());

        fileExternalIntegrationPort.removeFile(FILES_TRACKS_DESTINATION, externalId);
    }

    private void removeMusicCoverExternal(Music music) {
        if (music.getCoverContentType() == null || music.getCoverContentType().equals("unknown")) return;

        String externalId = music.getExternalIdentification().concat("_cover").concat(".").concat(music.getCoverContentType());

        fileExternalIntegrationPort.removeFile(FILES_IMAGES_DESTINATION, externalId);
    }
}
