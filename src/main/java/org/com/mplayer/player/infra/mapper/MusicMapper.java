package org.com.mplayer.player.infra.mapper;

import org.com.mplayer.player.domain.core.entity.Collection;
import org.com.mplayer.player.domain.core.entity.Lyric;
import org.com.mplayer.player.domain.core.entity.Music;
import org.com.mplayer.player.infra.interfaces.Mapper;
import org.com.mplayer.player.infra.persistence.entity.CollectionEntity;
import org.com.mplayer.player.infra.persistence.entity.LyricEntity;
import org.com.mplayer.player.infra.persistence.entity.MusicEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class MusicMapper implements Mapper<Music, MusicEntity> {
    @Override
    public Music toDomain(MusicEntity persistenceEntity) {
        if (persistenceEntity == null) return null;

        Music music = new Music();
        map(persistenceEntity, music);

        if (persistenceEntity.getLyric() != null) {
            Lyric lyric = new Lyric();
            map(persistenceEntity.getLyric(), lyric);
            music.setLyric(lyric);
        }

        if (persistenceEntity.getCollection() != null) {
            Collection collection = new Collection();
            map(persistenceEntity.getCollection(), collection);
            music.setCollection(collection);
        }

        return music;
    }

    @Override
    public MusicEntity toPersistence(Music domainEntity) {
        if (domainEntity == null) return null;

        MusicEntity music = new MusicEntity();
        map(domainEntity, music);

        if (domainEntity.getLyric() != null) {
            LyricEntity lyric = new LyricEntity();
            map(domainEntity.getLyric(), lyric);
            music.setLyric(lyric);
        }

        if (domainEntity.getCollection() != null) {
            CollectionEntity collection = new CollectionEntity();
            map(domainEntity.getCollection(), collection);
            music.setCollection(collection);
        }

        return music;
    }

    @Override
    public void map(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
