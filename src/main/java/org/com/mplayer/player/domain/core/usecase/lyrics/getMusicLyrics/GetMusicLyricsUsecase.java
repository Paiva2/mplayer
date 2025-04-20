package org.com.mplayer.player.domain.core.usecase.lyrics.getMusicLyrics;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Lyric;
import org.com.mplayer.player.domain.core.entity.Music;
import org.com.mplayer.player.domain.core.usecase.common.exception.MusicNotFoundException;
import org.com.mplayer.player.domain.core.usecase.lyrics.getMusicLyrics.exception.LyricNotFoundException;
import org.com.mplayer.player.domain.ports.in.usecase.GetMusicLyricsUsecasePort;
import org.com.mplayer.player.domain.ports.out.data.LyricDataProviderPort;
import org.com.mplayer.player.domain.ports.out.data.MusicDataProviderPort;
import org.com.mplayer.player.domain.ports.out.external.dto.GetMusicLyricsOutput;
import org.com.mplayer.player.infra.annotations.Usecase;

@Usecase
@AllArgsConstructor
public class GetMusicLyricsUsecase implements GetMusicLyricsUsecasePort {
    private final MusicDataProviderPort musicDataProviderPort;
    private final LyricDataProviderPort lyricDataProviderPort;

    @Override
    public GetMusicLyricsOutput execute(Long musicId) {
        Music music = findMusic(musicId);
        Lyric lyric = findLyric(music.getId());

        return mountOutput(lyric);
    }

    private Music findMusic(Long musicId) {
        return musicDataProviderPort.findById(musicId).orElseThrow(MusicNotFoundException::new);
    }

    private Lyric findLyric(Long musicId) {
        return lyricDataProviderPort.findByMusicId(musicId).orElseThrow(LyricNotFoundException::new);
    }

    private GetMusicLyricsOutput mountOutput(Lyric lyric) {
        return GetMusicLyricsOutput.builder()
            .id(lyric.getId())
            .lyric(lyric.getLyric())
            .createdAt(lyric.getCreatedAt())
            .build();
    }
}
