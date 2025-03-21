package org.com.mplayer.player.domain.ports.out.data;

import org.com.mplayer.player.domain.core.entity.Music;

import java.util.Optional;

public interface MusicDataProviderPort {
    Optional<Music> findMusicByUserAndArtistAndTrack(String externalUserId, String artist, String track, String contentType);

    Music persist(Music music);
}
