package org.com.mplayer.player.infra.adapter.data;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Playlist;
import org.com.mplayer.player.domain.ports.out.data.PlaylistDataProviderPort;
import org.com.mplayer.player.domain.ports.out.utils.PageData;
import org.com.mplayer.player.infra.mapper.PlaylistMapper;
import org.com.mplayer.player.infra.persistence.entity.PlaylistEntity;
import org.com.mplayer.player.infra.persistence.repository.PlaylistRepositoryOrm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class PlaylistDataProviderAdapter implements PlaylistDataProviderPort {
    private final PlaylistRepositoryOrm repository;

    private final PlaylistMapper mapper;

    @Override
    public Optional<Playlist> findByUserAndName(String externalUserId, String name) {
        Optional<PlaylistEntity> entity = repository.findByNameAndExternalUserId(name, externalUserId);
        if (entity.isEmpty()) return Optional.empty();

        return Optional.of(mapper.toDomain(entity.get()));
    }

    @Override
    public Playlist persist(Playlist playlist) {
        PlaylistEntity entity = repository.save(mapper.toPersistence(playlist));
        return mapper.toDomain(entity);
    }

    @Override
    public PageData<Playlist> findAllByUser(String externalUserId, int page, int size, String name, String direction, Boolean showOnlyPublic) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.fromString(direction), "ply_created_at");
        Page<PlaylistEntity> playlists = repository.findAllByUserId(externalUserId, name, showOnlyPublic, pageable);

        return new PageData<>(
            playlists.getNumber() + 1,
            playlists.getSize(),
            playlists.getTotalPages(),
            playlists.getTotalElements(),
            playlists.isLast(),
            playlists.getContent().stream().map(mapper::toDomain).toList()
        );
    }

    @Override
    public Optional<Playlist> findByIdAndUserId(long id, String externalUserId) {
        Optional<PlaylistEntity> entity = repository.findByIdAndExternalUserId(id, externalUserId);
        if (entity.isEmpty()) return Optional.empty();
        return Optional.of(mapper.toDomain(entity.get()));
    }

    @Override
    public void remove(Playlist playlist) {
        repository.deleteById(playlist.getId());
    }
}
