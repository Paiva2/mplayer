package org.com.mplayer.player.domain.ports.in.usecase;

import org.com.mplayer.player.domain.ports.in.external.RemoveMusicQueueInputPort;

public interface RemoveMusicQueueUsecasePort {
    void execute(RemoveMusicQueueInputPort input);
}
