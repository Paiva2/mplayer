package org.com.mplayer.player.infra.adapter.data;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.PlaylistMusic;
import org.com.mplayer.player.domain.ports.out.data.PlaylistMusicDataProviderPort;
import org.com.mplayer.player.domain.ports.out.utils.PageData;
import org.com.mplayer.player.infra.mapper.PlaylistMusicMapper;
import org.com.mplayer.player.infra.persistence.entity.PlaylistMusicEntity;
import org.com.mplayer.player.infra.persistence.repository.PlaylistMusicRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
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
    public PageData<PlaylistMusic> findAllByPlaylist(Long playlistId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "position");
        Page<PlaylistMusicEntity> playlistMusic = repository.findAllByPlaylistId(playlistId, pageable);

        return new PageData<>(
            playlistMusic.getNumber() + 1,
            playlistMusic.getSize(),
            playlistMusic.getTotalPages(),
            playlistMusic.getTotalElements(),
            playlistMusic.isLast(),
            playlistMusic.getContent().stream().map(mapper::toDomain).toList()
        );
    }

    @Override
    public PlaylistMusic persist(PlaylistMusic playlistMusic) {
        PlaylistMusicEntity persistence = repository.save(mapper.toPersistence(playlistMusic));
        return mapper.toDomain(persistence);
    }

    @Override
    public List<PlaylistMusic> persistAll(List<PlaylistMusic> playlistMusics) {
        List<PlaylistMusicEntity> playlistMusic = repository.saveAll(playlistMusics.stream().map(mapper::toPersistence).toList());
        return playlistMusic.stream().map(mapper::toDomain).toList();
    }

    @Override
    public Integer findLastPositionByPlaylistId(Long playlistId) {
        return repository.findLastPositionMusicByPlaylistId(playlistId);
    }

    @Override
    public List<PlaylistMusic> findAllbyPlaylistId(Long playlistId) {
        List<PlaylistMusicEntity> playlistMusic = repository.findAllByPlaylistId(playlistId);
        return playlistMusic.stream().map(mapper::toDomain).toList();
    }

    @Override
    public void removeAllByPlaylist(Long playlistId) {
        repository.deleteAllByPlaylistId(playlistId);
    }

    @Override
    public void removeByPlaylistAndMusic(Long playlistId, Long musicId) {
        repository.deleteByPlaylistIdAndMusicId(playlistId, musicId);
    }

    @Override
    public void updateDecreasePositionsHigher(Long playlistId, Integer position) {
        repository.updatePositionsDecreasingByPlaylistIdAndPosition(playlistId, position);
    }
}
