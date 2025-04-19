package org.com.mplayer.player.domain.ports.in.usecase;

public interface RemovePlaylistMusicUsecasePort {
    void execute(Long playlistId, Long musicId);
}
