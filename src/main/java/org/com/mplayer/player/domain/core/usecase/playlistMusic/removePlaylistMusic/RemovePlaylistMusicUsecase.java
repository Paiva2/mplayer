package org.com.mplayer.player.domain.core.usecase.playlistMusic.removePlaylistMusic;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Music;
import org.com.mplayer.player.domain.core.entity.Playlist;
import org.com.mplayer.player.domain.core.entity.PlaylistMusic;
import org.com.mplayer.player.domain.core.usecase.common.PlaylistMusicNotFoundException;
import org.com.mplayer.player.domain.core.usecase.common.exception.MusicNotFoundException;
import org.com.mplayer.player.domain.core.usecase.common.exception.PlaylistNotFoundException;
import org.com.mplayer.player.domain.ports.in.usecase.RemovePlaylistMusicUsecasePort;
import org.com.mplayer.player.domain.ports.out.data.MusicDataProviderPort;
import org.com.mplayer.player.domain.ports.out.data.PlaylistDataProviderPort;
import org.com.mplayer.player.domain.ports.out.data.PlaylistMusicDataProviderPort;
import org.com.mplayer.player.domain.ports.out.external.UserExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileOutputPort;
import org.com.mplayer.player.infra.annotations.Usecase;

@Usecase
@AllArgsConstructor
public class RemovePlaylistMusicUsecase implements RemovePlaylistMusicUsecasePort {
    private final UserExternalIntegrationPort userExternalIntegrationPort;

    private final PlaylistDataProviderPort playlistDataProviderPort;
    private final MusicDataProviderPort musicDataProviderPort;
    private final PlaylistMusicDataProviderPort playlistMusicDataProviderPort;

    @Override
    public void execute(Long playlistId, Long musicId) {
        FindUserExternalProfileOutputPort user = findUser();

        Playlist playlist = findPlaylist(playlistId, user.getId().toString());
        Music music = findMusic(user.getId().toString(), musicId);
        PlaylistMusic playlistMusic = findPlaylistMusic(playlist.getId(), music.getId());
        removePlaylistMusic(playlist, music);
        updatePositionsAfterRemoval(playlistMusic, playlist.getId());
    }

    private FindUserExternalProfileOutputPort findUser() {
        return userExternalIntegrationPort.findByExternalId();
    }

    private Playlist findPlaylist(Long playlistId, String userId) {
        return playlistDataProviderPort.findByIdAndUserId(playlistId, userId).orElseThrow(PlaylistNotFoundException::new);
    }

    private Music findMusic(String userId, Long musicId) {
        return musicDataProviderPort.findByIdAndUserId(musicId, userId).orElseThrow(MusicNotFoundException::new);
    }

    private PlaylistMusic findPlaylistMusic(Long playlistId, Long musicId) {
        return playlistMusicDataProviderPort.findByPlaylistAndMusic(playlistId, musicId).orElseThrow(PlaylistMusicNotFoundException::new);
    }

    private void removePlaylistMusic(Playlist playlist, Music music) {
        playlistMusicDataProviderPort.removeByPlaylistAndMusic(playlist.getId(), music.getId());
    }

    private void updatePositionsAfterRemoval(PlaylistMusic playlistMusic, Long playlistId) {
        playlistMusicDataProviderPort.updateDecreasePositionsHigher(playlistId, playlistMusic.getPosition());
    }
}
