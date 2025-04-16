package org.com.mplayer.player.domain.core.usecase.playlistMusic.insertPlaylistMusic;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Music;
import org.com.mplayer.player.domain.core.entity.Playlist;
import org.com.mplayer.player.domain.core.entity.PlaylistMusic;
import org.com.mplayer.player.domain.core.usecase.common.exception.MusicNotFoundException;
import org.com.mplayer.player.domain.core.usecase.common.exception.PlaylistNotFoundException;
import org.com.mplayer.player.domain.core.usecase.playlistMusic.insertPlaylistMusic.exception.MusicAlreadyAddedInPlaylistException;
import org.com.mplayer.player.domain.ports.in.usecase.InsertPlaylistMusicUsecasePort;
import org.com.mplayer.player.domain.ports.out.data.MusicDataProviderPort;
import org.com.mplayer.player.domain.ports.out.data.PlaylistDataProviderPort;
import org.com.mplayer.player.domain.ports.out.data.PlaylistMusicDataProviderPort;
import org.com.mplayer.player.domain.ports.out.external.UserExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileOutputPort;
import org.com.mplayer.player.infra.annotations.Usecase;

import java.util.Optional;

@Usecase
@AllArgsConstructor
public class InsertPlaylistMusicUsecase implements InsertPlaylistMusicUsecasePort {
    private final UserExternalIntegrationPort userExternalIntegrationPort;

    private final MusicDataProviderPort musicDataProviderPort;
    private final PlaylistDataProviderPort playlistDataProviderPort;
    private final PlaylistMusicDataProviderPort playlistMusicDataProviderPort;

    @Override
    public void execute(Long playlistId, Long musicId) {
        FindUserExternalProfileOutputPort user = findUser();

        Playlist playlist = findPlaylist(playlistId, user.getId().toString());

        Music music = findMusic(user.getId().toString(), musicId);
        checkMusicAlreadyInPlaylist(playlist, music);

        PlaylistMusic playlistMusic = fillPlaylistMusic(playlist, music);
        persistPlaylistMusic(playlistMusic);
    }

    private FindUserExternalProfileOutputPort findUser() {
        return userExternalIntegrationPort.findByExternalId();
    }

    private Playlist findPlaylist(Long playlistId, String externalUserId) {
        return playlistDataProviderPort.findByIdAndUserId(playlistId, externalUserId).orElseThrow(PlaylistNotFoundException::new);
    }

    private Music findMusic(String userId, Long musicId) {
        return musicDataProviderPort.findByIdAndUserId(musicId, userId).orElseThrow(MusicNotFoundException::new);
    }

    private void checkMusicAlreadyInPlaylist(Playlist playlist, Music music) {
        Optional<PlaylistMusic> playlistMusic = playlistMusicDataProviderPort.findByPlaylistAndMusic(playlist.getId(), music.getId());

        if (playlistMusic.isPresent()) {
            throw new MusicAlreadyAddedInPlaylistException(music.getId(), playlist.getId());
        }
    }

    private PlaylistMusic fillPlaylistMusic(Playlist playlist, Music music) {
        return PlaylistMusic.builder()
            .id(new PlaylistMusic.KeyId(playlist.getId(), music.getId()))
            .music(music)
            .playlist(playlist)
            .position(getLastPositionPlaylist(playlist) + 1)
            .build();
    }

    private Integer getLastPositionPlaylist(Playlist playlist) {
        return playlistMusicDataProviderPort.findLastPositionByPlaylistId(playlist.getId());
    }

    private void persistPlaylistMusic(PlaylistMusic playlistMusic) {
        playlistMusicDataProviderPort.persist(playlistMusic);
    }
}
