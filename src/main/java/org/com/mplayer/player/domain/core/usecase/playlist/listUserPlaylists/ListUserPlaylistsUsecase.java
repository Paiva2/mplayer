package org.com.mplayer.player.domain.core.usecase.playlist.listUserPlaylists;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Playlist;
import org.com.mplayer.player.domain.ports.in.usecase.ListUserPlaylistsUsecasePort;
import org.com.mplayer.player.domain.ports.out.data.PlaylistDataProviderPort;
import org.com.mplayer.player.domain.ports.out.external.UserExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileOutputPort;
import org.com.mplayer.player.domain.ports.out.external.dto.ListUserPlaylistsOutputPort;
import org.com.mplayer.player.domain.ports.out.utils.PageData;
import org.com.mplayer.player.infra.annotations.Usecase;

@Usecase
@AllArgsConstructor
public class ListUserPlaylistsUsecase implements ListUserPlaylistsUsecasePort {
    private final UserExternalIntegrationPort userExternalIntegrationPort;

    private final PlaylistDataProviderPort playlistDataProviderPort;

    @Override
    public PageData<ListUserPlaylistsOutputPort> execute(String userId, int page, int size, String name, String direction) {
        if (page < 1) {
            page = 1;
        }

        if (size < 5) {
            size = 5;
        }

        if (direction != null && !direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
            direction = "desc";
        }

        FindUserExternalProfileOutputPort user = userExternalIntegrationPort.findByExternalId();

        PageData<Playlist> playlists = findPlaylists(userId, page, size, name, direction, user);

        return mountOutput(playlists);
    }

    private PageData<Playlist> findPlaylists(String userId, int page, int size, String name, String direction, FindUserExternalProfileOutputPort user) {
        Boolean showOnlyPublic = null;

        if (!userId.equals(user.getId().toString())) {
            showOnlyPublic = true;
        }

        return playlistDataProviderPort.findAllByUser(userId, page, size, name, direction, showOnlyPublic);
    }

    private PageData<ListUserPlaylistsOutputPort> mountOutput(PageData<Playlist> playlists) {
        return new PageData<>(
            playlists.page(),
            playlists.size(),
            playlists.totalPages(),
            playlists.totalItems(),
            playlists.isLast(),
            playlists.list().stream().map(ListUserPlaylistsOutputPort::new).toList()
        );
    }
}
