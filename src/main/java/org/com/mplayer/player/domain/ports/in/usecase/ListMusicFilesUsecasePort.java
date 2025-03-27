package org.com.mplayer.player.domain.ports.in.usecase;

import org.com.mplayer.player.domain.ports.out.external.dto.ListMusicFilesDTO;

public interface ListMusicFilesUsecasePort {
    ListMusicFilesDTO execute(int page, int size, String title, String fileType, String artist, String order);
}
