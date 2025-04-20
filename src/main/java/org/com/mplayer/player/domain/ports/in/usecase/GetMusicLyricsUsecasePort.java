package org.com.mplayer.player.domain.ports.in.usecase;

import org.com.mplayer.player.domain.ports.out.external.dto.GetMusicLyricsOutput;

public interface GetMusicLyricsUsecasePort {
    GetMusicLyricsOutput execute(Long musicId);
}
