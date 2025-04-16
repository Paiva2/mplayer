package org.com.mplayer.player.domain.core.usecase.playlist.deletePlaylist;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Playlist;
import org.com.mplayer.player.domain.core.usecase.common.exception.PlaylistNotFoundException;
import org.com.mplayer.player.domain.ports.in.usecase.DeletePlaylistUsecasePort;
import org.com.mplayer.player.domain.ports.out.data.PlaylistDataProviderPort;
import org.com.mplayer.player.domain.ports.out.data.PlaylistMusicDataProviderPort;
import org.com.mplayer.player.domain.ports.out.external.FileExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.UserExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileOutputPort;
import org.com.mplayer.player.infra.annotations.Usecase;

@Usecase
@AllArgsConstructor
public class DeletePlaylistUsecase implements DeletePlaylistUsecasePort {
    private final static String FILES_IMAGES_DESTINATION = "mplayer-images";

    private final UserExternalIntegrationPort userExternalIntegrationPort;
    private final FileExternalIntegrationPort fileExternalIntegrationPort;

    private final PlaylistDataProviderPort playlistDataProviderPort;
    private final PlaylistMusicDataProviderPort playlistMusicDataProviderPort;

    @Override
    public void execute(Long playlistId) {
        FindUserExternalProfileOutputPort user = findUser();

        Playlist playlist = findPlaylist(playlistId, user.getId().toString());
        removePlaylistMusics(playlist);
        removePlaylist(playlist);

        if (playlist.getCoverUrl() != null) {
            removeCover(playlist);
        }
    }

    private FindUserExternalProfileOutputPort findUser() {
        return userExternalIntegrationPort.findByExternalId();
    }

    private Playlist findPlaylist(Long playlistId, String userId) {
        return playlistDataProviderPort.findByIdAndUserId(playlistId, userId).orElseThrow(PlaylistNotFoundException::new);
    }

    private void removePlaylistMusics(Playlist playlist) {
        playlistMusicDataProviderPort.removeAllByPlaylist(playlist.getId());
    }

    private void removePlaylist(Playlist playlist) {
        playlistDataProviderPort.remove(playlist);
    }

    private void removeCover(Playlist playlist) {
        fileExternalIntegrationPort.removeFile(FILES_IMAGES_DESTINATION, playlist.getExternalCoverId());
    }
}
