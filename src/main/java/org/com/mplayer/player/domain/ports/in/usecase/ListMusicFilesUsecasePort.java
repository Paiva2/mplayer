package org.com.mplayer.player.domain.ports.in.usecase;

import org.com.mplayer.player.domain.ports.out.external.dto.ListMusicFilesOutputPort;

public interface ListMusicFilesUsecasePort {
    ListMusicFilesOutputPort execute(int page, int size, String title, String fileType, String artist, String order);
}
