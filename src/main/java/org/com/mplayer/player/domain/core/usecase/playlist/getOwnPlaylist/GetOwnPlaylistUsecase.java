package org.com.mplayer.player.domain.core.usecase.playlist.getOwnPlaylist;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Playlist;
import org.com.mplayer.player.domain.core.entity.PlaylistMusic;
import org.com.mplayer.player.domain.core.usecase.common.exception.PlaylistNotFoundException;
import org.com.mplayer.player.domain.ports.in.usecase.GetOwnPlaylistUsecasePort;
import org.com.mplayer.player.domain.ports.out.data.PlaylistDataProviderPort;
import org.com.mplayer.player.domain.ports.out.data.PlaylistMusicDataProviderPort;
import org.com.mplayer.player.domain.ports.out.external.UserExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileOutputPort;
import org.com.mplayer.player.domain.ports.out.external.dto.GetOwnPlaylistOutputPort;
import org.com.mplayer.player.domain.ports.out.utils.PageData;
import org.com.mplayer.player.infra.annotations.Usecase;

@Usecase
@AllArgsConstructor
public class GetOwnPlaylistUsecase implements GetOwnPlaylistUsecasePort {
    private final UserExternalIntegrationPort userExternalIntegrationPort;

    private final PlaylistDataProviderPort playlistDataProviderPort;
    private final PlaylistMusicDataProviderPort playlistMusicDataProviderPort;

    @Override
    public GetOwnPlaylistOutputPort execute(Long playlistId, int page, int size) {
        FindUserExternalProfileOutputPort user = findUser();

        if (page < 1) {
            page = 1;
        }

        if (size < 5) {
            size = 5;
        }

        Playlist playlist = findPlaylist(user.getId().toString(), playlistId);
        PageData<PlaylistMusic> playlistMusics = findPlaylistMusics(playlist.getId(), page, size);

        return mountOutput(playlist, playlistMusics);
    }

    private FindUserExternalProfileOutputPort findUser() {
        return userExternalIntegrationPort.findByExternalId();
    }

    private Playlist findPlaylist(String userId, Long playlistId) {
        return playlistDataProviderPort.findByIdAndUserId(playlistId, userId).orElseThrow(PlaylistNotFoundException::new);
    }

    private PageData<PlaylistMusic> findPlaylistMusics(Long playlistId, int page, int size) {
        return playlistMusicDataProviderPort.findAllByPlaylist(playlistId, page, size);
    }

    private GetOwnPlaylistOutputPort mountOutput(Playlist playlist, PageData<PlaylistMusic> playlistMusics) {
        return GetOwnPlaylistOutputPort.builder()
            .id(playlist.getId())
            .name(playlist.getName())
            .coverUrl(playlist.getCoverUrl())
            .visiblePublic(playlist.getVisiblePublic())
            .createdAt(playlist.getCreatedAt())
            .updatedAt(playlist.getUpdatedAt())
            .pagination(GetOwnPlaylistOutputPort.PaginationOutput.builder()
                .page(playlistMusics.page())
                .size(playlistMusics.size())
                .totalItems(playlistMusics.totalItems())
                .totalPages(playlistMusics.totalPages())
                .isLast(playlistMusics.isLast())
                .build()
            ).musics(playlistMusics.list().stream().map(playlistMusic -> GetOwnPlaylistOutputPort.PlaylistMusicOutput.builder()
                    .musicId(playlistMusic.getMusic().getId())
                    .position(playlistMusic.getPosition())
                    .title(playlistMusic.getMusic().getTitle())
                    .artist(playlistMusic.getMusic().getArtist())
                    .coverUrl(playlistMusic.getMusic().getCoverUrl())
                    .durationSeconds(playlistMusic.getMusic().getDurationSeconds())
                    .fileType(playlistMusic.getMusic().getFileType())
                    .createdAt(playlistMusic.getMusic().getCreatedAt())
                    .collection(playlistMusic.getMusic().getCollection() != null ? GetOwnPlaylistOutputPort.PlaylistMusicOutput.CollectionOutput.builder()
                        .id(playlistMusic.getMusic().getCollection().getId())
                        .title(playlistMusic.getMusic().getCollection().getTitle())
                        .build() : null)
                    .build())
                .toList()
            ).build();
    }
}
