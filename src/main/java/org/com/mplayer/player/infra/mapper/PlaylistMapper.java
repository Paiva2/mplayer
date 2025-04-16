package org.com.mplayer.player.infra.mapper;

import org.com.mplayer.player.domain.core.entity.Playlist;
import org.com.mplayer.player.domain.core.entity.PlaylistMusic;
import org.com.mplayer.player.infra.interfaces.Mapper;
import org.com.mplayer.player.infra.persistence.entity.PlaylistEntity;
import org.com.mplayer.player.infra.persistence.entity.PlaylistMusicEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlaylistMapper implements Mapper<Playlist, PlaylistEntity> {
    @Override
    public Playlist toDomain(PlaylistEntity persistenceEntity) {
        if (persistenceEntity == null) return null;

        Playlist playlist = new Playlist();
        map(persistenceEntity, playlist);

        if (persistenceEntity.getPlaylistMusics() != null) {
            List<PlaylistMusic> playlistMusics = new ArrayList<>();

            for (PlaylistMusicEntity musicEntity : persistenceEntity.getPlaylistMusics()) {
                PlaylistMusic music = new PlaylistMusic();
                map(musicEntity, music);

                playlistMusics.add(music);
            }

            playlist.setPlaylistMusics(playlistMusics);
        }

        return playlist;
    }

    @Override
    public PlaylistEntity toPersistence(Playlist domainEntity) {
        if (domainEntity == null) return null;

        PlaylistEntity playlist = new PlaylistEntity();
        map(domainEntity, playlist);

        return playlist;
    }

    @Override
    public void map(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
