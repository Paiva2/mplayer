package org.com.mplayer.player.domain.ports.in.usecase;

public interface ChangePlaylistMusicPositionPort {
    void execute(Long playlistId, Long musicId, Integer currentPosition, Integer newPosition);
}
