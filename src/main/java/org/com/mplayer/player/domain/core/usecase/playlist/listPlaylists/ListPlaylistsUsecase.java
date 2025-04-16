package org.com.mplayer.player.domain.core.usecase.playlist.listPlaylists;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Playlist;
import org.com.mplayer.player.domain.ports.in.usecase.ListPlaylistsUsecasePort;
import org.com.mplayer.player.domain.ports.out.data.PlaylistDataProviderPort;
import org.com.mplayer.player.domain.ports.out.external.UserExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileOutputPort;
import org.com.mplayer.player.domain.ports.out.external.dto.PlaylistOutputPort;
import org.com.mplayer.player.domain.ports.out.utils.PageData;
import org.com.mplayer.player.infra.annotations.Usecase;

@Usecase
@AllArgsConstructor
public class ListPlaylistsUsecase implements ListPlaylistsUsecasePort {
    private final UserExternalIntegrationPort userExternalIntegrationPort;

    private final PlaylistDataProviderPort playlistDataProviderPort;

    @Override
    public PageData<PlaylistOutputPort> execute(int page, int size, String name, String direction) {
        FindUserExternalProfileOutputPort user = findUser();

        if (page < 1) {
            page = 1;
        }

        if (size > 50) {
            size = 50;
        } else if (size < 5) {
            size = 5;
        }

        if (direction == null || !direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
            direction = "DESC";
        }

        PageData<Playlist> playlists = findPlaylists(user.getId().toString(), page, size, name, direction);

        return mountOutput(playlists);
    }

    private FindUserExternalProfileOutputPort findUser() {
        return userExternalIntegrationPort.findByExternalId();
    }

    private PageData<Playlist> findPlaylists(String userId, int page, int size, String name, String direction) {
        return playlistDataProviderPort.findAllByUser(userId, page, size, name, direction);
    }

    private PageData<PlaylistOutputPort> mountOutput(PageData<Playlist> playlists) {
        return new PageData<>(
            playlists.page(),
            playlists.size(),
            playlists.totalPages(),
            playlists.totalItems(),
            playlists.isLast(),
            playlists.list().stream().map(PlaylistOutputPort::new).toList()
        );
    }
}
