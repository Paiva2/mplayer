package org.com.mplayer.player.domain.ports.in.usecase;

import org.springframework.web.multipart.MultipartFile;

public interface InsertMusicUsecasePort {
    void execute(MultipartFile musicFile);
}
