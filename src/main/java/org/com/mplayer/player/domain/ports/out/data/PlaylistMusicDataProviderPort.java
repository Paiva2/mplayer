package org.com.mplayer.player.domain.ports.out.data;

import org.com.mplayer.player.domain.core.entity.PlaylistMusic;
import org.com.mplayer.player.domain.ports.out.utils.PageData;

import java.util.List;
import java.util.Optional;

public interface PlaylistMusicDataProviderPort {
    Optional<PlaylistMusic> findByPlaylistAndMusic(Long playlistId, Long musicId);

    PageData<PlaylistMusic> findAllByPlaylist(Long playlistId, int page, int size);

    PlaylistMusic persist(PlaylistMusic playlistMusic);

    List<PlaylistMusic> persistAll(List<PlaylistMusic> playlistMusics);

    Integer findLastPositionByPlaylistId(Long playlistId);

    List<PlaylistMusic> findAllbyPlaylistId(Long playlistId);

    void removeAllByPlaylist(Long playlistId);

    void removeByPlaylistAndMusic(Long playlistId, Long musicId);

    void updateDecreasePositionsHigher(Long playlistId, Integer position);
}
