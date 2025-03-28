package org.com.mplayer.player.domain.ports.in.usecase;

import org.com.mplayer.player.domain.ports.out.external.dto.GetMusicDTO;

public interface GetMusicUsecasePort {
    GetMusicDTO execute(Long musicId);
}
