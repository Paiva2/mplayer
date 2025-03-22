package org.com.mplayer.player.infra.adapter.data;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Lyric;
import org.com.mplayer.player.domain.ports.out.data.LyricDataProviderPort;
import org.com.mplayer.player.infra.mapper.LyricMapper;
import org.com.mplayer.player.infra.persistence.entity.LyricEntity;
import org.com.mplayer.player.infra.persistence.repository.LyricRepositoryOrm;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class LyricDataProviderAdapter implements LyricDataProviderPort {
    private final LyricRepositoryOrm repository;

    private final LyricMapper mapper;

    @Override
    public Optional<Lyric> findLyricByArtistAndMusicTitle(String artist, String trackTitle) {
        Optional<LyricEntity> lyric = repository.findByArtistAndMusicTitle(artist, trackTitle);
        if (lyric.isEmpty()) return Optional.empty();

        return Optional.of(mapper.toDomain(lyric.get()));
    }

    @Override
    public Lyric persist(Lyric lyric) {
        LyricEntity newLyric = repository.save(mapper.toPersistence(lyric));

        return mapper.toDomain(newLyric);
    }

    @Override
    public void removeByMusicId(Long musicId) {
        repository.deleteByMusicId(musicId);
    }
}
