package org.com.mplayer.player.domain.core.usecase.playlist.createPlaylist;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Playlist;
import org.com.mplayer.player.domain.core.usecase.playlist.createPlaylist.exception.PlaylistAlreadyExistsException;
import org.com.mplayer.player.domain.ports.in.external.CreatePlaylistInputPort;
import org.com.mplayer.player.domain.ports.in.usecase.CreatePlaylistUsecasePort;
import org.com.mplayer.player.domain.ports.out.data.PlaylistDataProviderPort;
import org.com.mplayer.player.domain.ports.out.external.FileExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.UserExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileOutputPort;
import org.com.mplayer.player.domain.ports.out.utils.FileUtilsPort;
import org.com.mplayer.player.infra.annotations.Usecase;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Usecase
@AllArgsConstructor
public class CreatePlaylistUsecase implements CreatePlaylistUsecasePort {
    private final static String FILES_IMAGES_DESTINATION = "mplayer-images";

    private final PlaylistDataProviderPort playlistDataProviderPort;

    private final UserExternalIntegrationPort userExternalIntegrationPort;
    private final FileExternalIntegrationPort fileExternalIntegrationPort;

    private final FileUtilsPort fileUtilsPort;

    @Override
    public void execute(CreatePlaylistInputPort input) {
        FindUserExternalProfileOutputPort user = findUser();

        checkPlaylistAlreadyExists(user.getId().toString(), input.getName());

        String coverUrl = null;
        String externalCoverId = null;

        if (input.getCover() != null) {
            externalCoverId = getExternalCoverId(user.getId().toString(), input.getCover());
            coverUrl = uploadCoverImage(input.getCover(), externalCoverId);
        }

        Playlist playlist = fillPlaylist(input, coverUrl, externalCoverId, user.getId().toString());
        persistPlaylist(playlist);
    }

    private FindUserExternalProfileOutputPort findUser() {
        return userExternalIntegrationPort.findByExternalId();
    }

    private void checkPlaylistAlreadyExists(String userId, String playlistName) {
        Optional<Playlist> playlist = playlistDataProviderPort.findByUserAndName(userId, playlistName);

        if (playlist.isPresent()) {
            throw new PlaylistAlreadyExistsException();
        }
    }

    private String getExternalCoverId(String userId, MultipartFile multipartFile) {
        StringBuilder builder = new StringBuilder();
        builder.append(userId).append("_");
        builder.append(UUID.randomUUID()).append("_");
        builder.append(fileUtilsPort.fileNameWithoutExtension(multipartFile)).append("_");
        builder.append("cover_playlist").append(".");
        builder.append(fileUtilsPort.fileExtension(multipartFile));

        return builder.toString();
    }

    private String uploadCoverImage(MultipartFile multipartFile, String externalCoverId) {
        return fileExternalIntegrationPort.insertFile(multipartFile, externalCoverId, FILES_IMAGES_DESTINATION, multipartFile.getContentType());
    }

    private Playlist fillPlaylist(CreatePlaylistInputPort input, String coverUrl, String externalCoverId, String userId) {
        return Playlist.builder()
            .name(input.getName().trim())
            .coverUrl(coverUrl)
            .externalUserId(userId)
            .visiblePublic(input.getVisiblePublic())
            .externalCoverId(externalCoverId)
            .build();
    }

    private Playlist persistPlaylist(Playlist playlist) {
        return playlistDataProviderPort.persist(playlist);
    }
}
