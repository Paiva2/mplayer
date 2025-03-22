package org.com.mplayer.player.infra.adapter.data;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Music;
import org.com.mplayer.player.domain.core.enums.EFileType;
import org.com.mplayer.player.domain.ports.out.data.MusicDataProviderPort;
import org.com.mplayer.player.infra.mapper.MusicMapper;
import org.com.mplayer.player.infra.persistence.entity.MusicEntity;
import org.com.mplayer.player.infra.persistence.repository.MusicRepositoryOrm;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class MusicDataProviderAdapter implements MusicDataProviderPort {
    private final MusicRepositoryOrm repository;

    private final MusicMapper mapper;
    private final MusicMapper musicMapper;

    @Override
    public Optional<Music> findMusicByUserAndArtistAndTrack(String externalUserId, String artist, String track, String contentType) {
        Optional<MusicEntity> music = repository.findByUserIdAndArtistAndTrack(externalUserId, artist, track, EFileType.valueOf(contentType));
        if (music.isEmpty()) return Optional.empty();
        return Optional.of(mapper.toDomain(music.get()));
    }

    @Override
    public Music persist(Music music) {
        MusicEntity newMusic = repository.save(mapper.toPersistence(music));
        return mapper.toDomain(newMusic);
    }

    @Override
    public Optional<Music> findById(Long id) {
        Optional<MusicEntity> music = repository.findById(id);
        if (music.isEmpty()) return Optional.empty();
        return Optional.of(mapper.toDomain(music.get()));
    }

    @Override
    public Optional<Music> findByIdWithDeps(Long id) {
        Optional<MusicEntity> music = repository.findByIdWithDeps(id);
        if (music.isEmpty()) return Optional.empty();
        return Optional.of(mapper.toDomain(music.get()));
    }

    @Override
    public void remove(Music music) {
        repository.delete(musicMapper.toPersistence(music));
    }
}
