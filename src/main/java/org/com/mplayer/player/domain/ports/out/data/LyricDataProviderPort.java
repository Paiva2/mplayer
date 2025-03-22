package org.com.mplayer.player.domain.ports.out.data;

import org.com.mplayer.player.domain.core.entity.Lyric;

import java.util.Optional;

public interface LyricDataProviderPort {
    Optional<Lyric> findLyricByArtistAndMusicTitle(String artist, String trackTitle);

    Lyric persist(Lyric lyric);

    void removeByMusicId(Long musicId);
}
