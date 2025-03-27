package org.com.mplayer.player.domain.ports.out.data;

import org.com.mplayer.player.domain.core.entity.Music;
import org.com.mplayer.player.domain.core.enums.EFileType;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface MusicDataProviderPort {
    Optional<Music> findMusicByUserAndArtistAndTrack(String externalUserId, String artist, String track, String contentType);

    Music persist(Music music);

    Optional<Music> findById(Long id);

    Optional<Music> findByIdWithDeps(Long id);

    void remove(Music music);

    Page<Music> findAllByExternalUser(int page, int size, String externalUserId, String title, EFileType fileType, String artist, String groupBy);
}
