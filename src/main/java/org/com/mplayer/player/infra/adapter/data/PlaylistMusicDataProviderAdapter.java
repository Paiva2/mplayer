package org.com.mplayer.player.infra.adapter.data;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.PlaylistMusic;
import org.com.mplayer.player.domain.ports.out.data.PlaylistMusicDataProviderPort;
import org.com.mplayer.player.infra.mapper.PlaylistMusicMapper;
import org.com.mplayer.player.infra.persistence.entity.PlaylistMusicEntity;
import org.com.mplayer.player.infra.persistence.repository.PlaylistMusicRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class PlaylistMusicDataProviderAdapter implements PlaylistMusicDataProviderPort {
    private final PlaylistMusicRepository repository;

    private final PlaylistMusicMapper mapper;

    @Override
    public Optional<PlaylistMusic> findByPlaylistAndMusic(Long playlistId, Long musicId) {
        Optional<PlaylistMusicEntity> playlistMusic = repository.findByPlaylistIdAndMusicId(playlistId, musicId);
        if (playlistMusic.isEmpty()) return Optional.empty();
        return Optional.of(mapper.toDomain(playlistMusic.get()));
    }

    @Override
    public PlaylistMusic persist(PlaylistMusic playlistMusic) {
        PlaylistMusicEntity persistence = repository.save(mapper.toPersistence(playlistMusic));
        return mapper.toDomain(persistence);
    }

    @Override
    public Integer findLastPositionByPlaylistId(Long playlistId) {
        return repository.findLastPositionMusicByPlaylistId(playlistId);
    }

    @Override
    public void removeAllByPlaylist(Long playlistId) {
        repository.deleteAllByPlaylistId(playlistId);
    }
}
