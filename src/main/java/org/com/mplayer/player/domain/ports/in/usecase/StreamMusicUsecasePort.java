package org.com.mplayer.player.domain.ports.in.usecase;

import org.com.mplayer.player.domain.ports.out.external.dto.StreamMusicDTO;

public interface StreamMusicUsecasePort {
    StreamMusicDTO execute(Long musicId, String byteRange);
}
