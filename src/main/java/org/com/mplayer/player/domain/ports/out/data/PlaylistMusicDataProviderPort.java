package org.com.mplayer.player.domain.ports.out.data;

import org.com.mplayer.player.domain.core.entity.PlaylistMusic;

import java.util.Optional;

public interface PlaylistMusicDataProviderPort {
    Optional<PlaylistMusic> findByPlaylistAndMusic(Long playlistId, Long musicId);

    PlaylistMusic persist(PlaylistMusic playlistMusic);

    Integer findLastPositionByPlaylistId(Long playlistId);

    void removeAllByPlaylist(Long playlistId);
}
