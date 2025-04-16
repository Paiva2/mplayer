package org.com.mplayer.player.domain.core.usecase.music.listMusicFiles;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Music;
import org.com.mplayer.player.domain.core.enums.EFileType;
import org.com.mplayer.player.domain.ports.in.usecase.ListMusicFilesUsecasePort;
import org.com.mplayer.player.domain.ports.out.external.UserExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileOutputPort;
import org.com.mplayer.player.domain.ports.out.external.dto.ListMusicFilesOutputPort;
import org.com.mplayer.player.infra.adapter.data.MusicDataProviderAdapter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ListMusicFilesUsecase implements ListMusicFilesUsecasePort {
    private final UserExternalIntegrationPort userExternalIntegrationPort;
    private final MusicDataProviderAdapter musicDataProviderAdapter;

    @Override
    public ListMusicFilesOutputPort execute(int page, int size, String title, String fileType, String artist, String order) {
        if (page < 1) {
            page = 1;
        }

        if (size < 30) {
            size = 30;
        } else if (size > 100) {
            size = 100;
        }

        FindUserExternalProfileOutputPort user = findUser();

        EFileType fileTypeFilter = checkFileType(fileType);

        Page<Music> musics = findMusics(page, size, user.getId().toString(), title, fileTypeFilter, artist, order);

        return mountOutput(musics);
    }

    private FindUserExternalProfileOutputPort findUser() {
        return userExternalIntegrationPort.findByExternalId();
    }

    private EFileType checkFileType(String fileType) {
        if (fileType == null) return null;

        try {
            return EFileType.valueOf(fileType.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    private Page<Music> findMusics(int page, int size, String externalUserId, String title, EFileType fileType, String artist, String orderBy) {
        return musicDataProviderAdapter.findAllByExternalUser(page, size, externalUserId, title, fileType, artist, orderBy);
    }

    private ListMusicFilesOutputPort mountOutput(Page<Music> musics) {
        return ListMusicFilesOutputPort.builder()
            .page(musics.getNumber() + 1)
            .size(musics.getSize())
            .totalMusics(musics.getTotalElements())
            .totalPages(musics.getTotalPages())
            .musics(musics.getContent().stream().map(ListMusicFilesOutputPort.MusicDTO::new).toList())
            .build();
    }
}
