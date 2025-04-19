package org.com.mplayer.player.domain.core.usecase.playlistMusic.changePlaylistMusicPosition;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Playlist;
import org.com.mplayer.player.domain.core.entity.PlaylistMusic;
import org.com.mplayer.player.domain.core.usecase.common.PlaylistMusicNotFoundException;
import org.com.mplayer.player.domain.core.usecase.common.exception.PlaylistNotFoundException;
import org.com.mplayer.player.domain.ports.in.usecase.ChangePlaylistMusicPositionPort;
import org.com.mplayer.player.domain.ports.out.data.PlaylistDataProviderPort;
import org.com.mplayer.player.domain.ports.out.data.PlaylistMusicDataProviderPort;
import org.com.mplayer.player.domain.ports.out.external.UserExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileOutputPort;
import org.com.mplayer.player.infra.annotations.Usecase;

import java.util.List;
import java.util.Optional;

@Usecase
@AllArgsConstructor
public class ChangePlaylistMusicPosition implements ChangePlaylistMusicPositionPort {
    private final UserExternalIntegrationPort userExternalIntegrationPort;

    private final PlaylistDataProviderPort playlistDataProviderPort;
    private final PlaylistMusicDataProviderPort playlistMusicDataProviderPort;

    @Override
    public void execute(Long playlistId, Long musicId, Integer currentPosition, Integer newPosition) {
        FindUserExternalProfileOutputPort user = findUser();

        Playlist playlist = findPlaylist(user.getId().toString(), playlistId);
        List<PlaylistMusic> playlistMusics = findPlaylistMusics(playlist);
        PlaylistMusic playlistMusic = findPlaylistMusic(playlist.getId(), musicId, currentPosition, playlistMusics);

        if (newPosition < 1) {
            newPosition = 1;
        }

        if (newPosition > playlistMusics.size()) {
            newPosition = playlistMusics.size();
        }

        changePositions(playlistMusics, playlistMusic, newPosition);
        persistPlaylistMusics(playlistMusics);
    }

    private FindUserExternalProfileOutputPort findUser() {
        return userExternalIntegrationPort.findByExternalId();
    }

    private Playlist findPlaylist(String userExternalId, Long playlistId) {
        return playlistDataProviderPort.findByIdAndUserId(playlistId, userExternalId).orElseThrow(PlaylistNotFoundException::new);
    }

    private PlaylistMusic findPlaylistMusic(Long playlistId, Long musicId, Integer currentPosition, List<PlaylistMusic> playlistMusics) {
        Optional<PlaylistMusic> playlistMusic = playlistMusics.stream().filter(plm -> plm.getId().getMusicId().equals(musicId) && plm.getId().getPlaylistId().equals(playlistId) && plm.getPosition().equals(currentPosition)).findFirst();

        if (playlistMusic.isEmpty()) {
            throw new PlaylistMusicNotFoundException();
        }

        return playlistMusic.get();
    }

    private List<PlaylistMusic> findPlaylistMusics(Playlist playlist) {
        return playlistMusicDataProviderPort.findAllbyPlaylistId(playlist.getId());
    }

    private void changePositions(List<PlaylistMusic> playlistMusics, PlaylistMusic playlistMusicChanging, Integer newPosition) {
        Integer currentPosition = playlistMusicChanging.getPosition();

        for (PlaylistMusic playlistMusic : playlistMusics) {
            if (playlistMusic.getId().getMusicId().equals(playlistMusicChanging.getId().getMusicId()) && playlistMusic.getId().getPlaylistId().equals(playlistMusicChanging.getId().getPlaylistId()) && playlistMusic.getPosition().equals(currentPosition)) {
                playlistMusic.setPosition(newPosition);
            } else if (newPosition > currentPosition) {
                if (playlistMusic.getPosition() > currentPosition && playlistMusic.getPosition() <= newPosition) {
                    playlistMusic.setPosition(playlistMusic.getPosition() - 1);
                }
            } else {
                if (playlistMusic.getPosition() >= newPosition && playlistMusic.getPosition() < currentPosition) {
                    playlistMusic.setPosition(playlistMusic.getPosition() + 1);
                }
            }
        }
    }

    private void persistPlaylistMusics(List<PlaylistMusic> playlistMusics) {
        playlistMusicDataProviderPort.persistAll(playlistMusics);
    }
}
