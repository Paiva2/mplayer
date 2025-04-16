package org.com.mplayer.player.infra.mapper;

import org.com.mplayer.player.domain.core.entity.Music;
import org.com.mplayer.player.domain.core.entity.Playlist;
import org.com.mplayer.player.domain.core.entity.PlaylistMusic;
import org.com.mplayer.player.infra.interfaces.Mapper;
import org.com.mplayer.player.infra.persistence.entity.MusicEntity;
import org.com.mplayer.player.infra.persistence.entity.PlaylistEntity;
import org.com.mplayer.player.infra.persistence.entity.PlaylistMusicEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class PlaylistMusicMapper implements Mapper<PlaylistMusic, PlaylistMusicEntity> {
    @Override
    public PlaylistMusic toDomain(PlaylistMusicEntity persistenceEntity) {
        if (persistenceEntity == null) return null;

        PlaylistMusic playlistMusic = new PlaylistMusic();
        map(persistenceEntity, playlistMusic);

        if (persistenceEntity.getId() != null) {
            PlaylistMusic.KeyId id = new PlaylistMusic.KeyId();
            map(persistenceEntity.getId(), id);

            playlistMusic.setId(id);
        }

        if (persistenceEntity.getMusic() != null) {
            Music music = new Music();
            map(persistenceEntity.getMusic(), music);

            playlistMusic.setMusic(music);
        }

        if (persistenceEntity.getPlaylist() != null) {
            Playlist playlist = new Playlist();
            map(persistenceEntity.getPlaylist(), playlist);

            playlistMusic.setPlaylist(playlist);
        }

        return playlistMusic;
    }

    @Override
    public PlaylistMusicEntity toPersistence(PlaylistMusic domainEntity) {
        if (domainEntity == null) return null;

        PlaylistMusicEntity playlistMusic = new PlaylistMusicEntity();
        map(domainEntity, playlistMusic);

        if (domainEntity.getId() != null) {
            PlaylistMusicEntity.KeyId id = new PlaylistMusicEntity.KeyId();
            map(domainEntity.getId(), id);

            playlistMusic.setId(id);
        }

        if (domainEntity.getMusic() != null) {
            MusicEntity music = new MusicEntity();
            map(domainEntity.getMusic(), music);

            playlistMusic.setMusic(music);
        }

        if (domainEntity.getPlaylist() != null) {
            PlaylistEntity playlist = new PlaylistEntity();
            map(domainEntity.getPlaylist(), playlist);

            playlistMusic.setPlaylist(playlist);
        }

        return playlistMusic;
    }

    @Override
    public void map(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
