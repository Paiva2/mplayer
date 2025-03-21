package org.com.mplayer.player.infra.mapper;

import org.com.mplayer.player.domain.core.entity.Lyric;
import org.com.mplayer.player.domain.core.entity.Music;
import org.com.mplayer.player.infra.interfaces.Mapper;
import org.com.mplayer.player.infra.persistence.entity.LyricEntity;
import org.com.mplayer.player.infra.persistence.entity.MusicEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class LyricMapper implements Mapper<Lyric, LyricEntity> {
    @Override
    public Lyric toDomain(LyricEntity persistenceEntity) {
        if (persistenceEntity == null) return null;
        Lyric lyric = new Lyric();
        map(persistenceEntity, lyric);

        if (persistenceEntity.getMusic() != null) {
            Music music = new Music();
            map(persistenceEntity.getMusic(), music);
            lyric.setMusic(music);
        }

        return lyric;
    }

    @Override
    public LyricEntity toPersistence(Lyric domainEntity) {
        if (domainEntity == null) return null;
        LyricEntity lyric = new LyricEntity();
        map(domainEntity, lyric);

        if (domainEntity.getMusic() != null) {
            MusicEntity music = new MusicEntity();
            map(domainEntity.getMusic(), music);
            lyric.setMusic(music);
        }

        return lyric;
    }

    @Override
    public void map(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
