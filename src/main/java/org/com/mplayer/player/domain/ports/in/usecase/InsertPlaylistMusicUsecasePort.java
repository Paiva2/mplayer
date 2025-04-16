package org.com.mplayer.player.domain.ports.in.usecase;

public interface InsertPlaylistMusicUsecasePort {
    void execute(Long playlistId, Long musicId);
}
