package org.com.mplayer.player.infra.mapper;

import org.com.mplayer.player.domain.core.entity.Collection;
import org.com.mplayer.player.domain.core.entity.Lyric;
import org.com.mplayer.player.domain.core.entity.Music;
import org.com.mplayer.player.infra.interfaces.Mapper;
import org.com.mplayer.player.infra.persistence.entity.CollectionEntity;
import org.com.mplayer.player.infra.persistence.entity.MusicEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

            if (persistenceEntity.getCollection().getMusics() != null) {
                List<Music> collectionMusics = new ArrayList<>();

                for (MusicEntity musicFromCollection : persistenceEntity.getCollection().getMusics()) {
                    Music musicConverted = new Music();
                    map(musicFromCollection, musicConverted);
                    collectionMusics.add(musicConverted);
                }

                music.getCollection().setMusics(collectionMusics);
            }
        }

        return music;
    }

    @Override
    public MusicEntity toPersistence(Music domainEntity) {
        if (domainEntity == null) return null;

        MusicEntity music = new MusicEntity();
        map(domainEntity, music);

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
