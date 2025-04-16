package org.com.mplayer.player.domain.ports.out.data;

import org.com.mplayer.player.domain.core.entity.Playlist;
import org.com.mplayer.users.domain.ports.out.utils.PageData;

import java.util.Optional;

public interface PlaylistDataProviderPort {
    Optional<Playlist> findByUserAndName(String externalUserId, String name);

    Playlist persist(Playlist playlist);

    PageData<Playlist> findAllByUser(String externalUserId, int page, int size, String name, String direction);

    Optional<Playlist> findByIdAndUserId(long id, String externalUserId);

    void remove(Playlist playlist);
}
