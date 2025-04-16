package org.com.mplayer.player.infra.adapter.data;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Music;
import org.com.mplayer.player.domain.core.enums.EFileType;
import org.com.mplayer.player.domain.core.enums.EOrderBy;
import org.com.mplayer.player.domain.ports.out.data.MusicDataProviderPort;
import org.com.mplayer.player.infra.mapper.MusicMapper;
import org.com.mplayer.player.infra.persistence.entity.MusicEntity;
import org.com.mplayer.player.infra.persistence.repository.MusicRepositoryOrm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Optional<Music> findByIdAndUserId(Long id, String externalUserId) {
        Optional<MusicEntity> music = repository.findByIdAndExternalUserId(id, externalUserId);
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

    @Override
    public Page<Music> findAllByExternalUser(int page, int size, String externalUserId, String title, EFileType fileType, String artist, String orderBy) {
        String order = "mus_created_at";
        String type = fileType != null ? fileType.name() : null;

        if (orderBy != null) {
            if (orderBy.toUpperCase().equals(EOrderBy.ARTIST.name())) {
                order = "mus_artist";
            } else if (orderBy.toUpperCase().equals(EOrderBy.TITLE.name())) {
                order = "mus_title";
            }
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, order);
        Page<MusicEntity> musicEntities = repository.findAllByExternalUserFiltering(externalUserId, title, type, artist, pageable);

        return musicEntities.map(musicMapper::toDomain);
    }
}
