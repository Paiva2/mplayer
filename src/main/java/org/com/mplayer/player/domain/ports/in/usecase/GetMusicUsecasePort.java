package org.com.mplayer.player.domain.ports.in.usecase;

import org.com.mplayer.player.domain.ports.out.external.dto.GetMusicOutputPort;

public interface GetMusicUsecasePort {
    GetMusicOutputPort execute(Long musicId);
}
