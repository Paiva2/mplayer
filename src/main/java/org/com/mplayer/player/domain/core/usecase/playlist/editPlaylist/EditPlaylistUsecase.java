package org.com.mplayer.player.domain.core.usecase.playlist.editPlaylist;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Playlist;
import org.com.mplayer.player.domain.core.usecase.common.exception.PlaylistNotFoundException;
import org.com.mplayer.player.domain.ports.in.external.EditPlaylistInputPort;
import org.com.mplayer.player.domain.ports.in.usecase.EditPlaylistUsecasePort;
import org.com.mplayer.player.domain.ports.out.data.PlaylistDataProviderPort;
import org.com.mplayer.player.domain.ports.out.external.FileExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.UserExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileOutputPort;
import org.com.mplayer.player.domain.ports.out.utils.FileUtilsPort;
import org.com.mplayer.player.infra.annotations.Usecase;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Usecase
@AllArgsConstructor
public class EditPlaylistUsecase implements EditPlaylistUsecasePort {
    private final static String FILES_IMAGES_DESTINATION = "mplayer-images";

    private final UserExternalIntegrationPort userExternalIntegrationPort;
    private final FileExternalIntegrationPort fileExternalIntegrationPort;

    private final PlaylistDataProviderPort playlistDataProviderPort;

    private final FileUtilsPort fileUtilsPort;

    @Override
    public void execute(Long playlistId, EditPlaylistInputPort input) {
        FindUserExternalProfileOutputPort user = findUser();

        Playlist playlist = findPlaylist(playlistId, user.getId().toString());
        editPlaylist(playlist, input, user.getId().toString());
        persistPlaylist(playlist);
    }

    private FindUserExternalProfileOutputPort findUser() {
        return userExternalIntegrationPort.findByExternalId();
    }

    private Playlist findPlaylist(Long playlistId, String userId) {
        return playlistDataProviderPort.findByIdAndUserId(playlistId, userId).orElseThrow(PlaylistNotFoundException::new);
    }

    private void editPlaylist(Playlist playlist, EditPlaylistInputPort input, String userId) {
        playlist.setName(input.getName() != null ? input.getName() : playlist.getName());
        playlist.setVisiblePublic(input.getVisiblePublic() != null ? input.getVisiblePublic() : playlist.getVisiblePublic());

        if (input.getCover() != null) {
            if (playlist.getCoverUrl() != null) {
                removeCurrentCover(playlist.getExternalCoverId());
            }

            String newCoverExternalId = generateCoverExternalId(userId, input.getCover());
            String coverUrl = uploadNewCover(input, newCoverExternalId);

            playlist.setExternalCoverId(newCoverExternalId);
            playlist.setCoverUrl(coverUrl);
        } else {
            removeCurrentCover(playlist.getExternalCoverId());
            playlist.setExternalCoverId(null);
            playlist.setCoverUrl(null);
        }
    }

    private void removeCurrentCover(String coverId) {
        fileExternalIntegrationPort.removeFile(FILES_IMAGES_DESTINATION, coverId);
    }

    private String generateCoverExternalId(String userId, MultipartFile file) {
        StringBuilder builder = new StringBuilder();
        builder.append(userId).append("_");
        builder.append(UUID.randomUUID()).append("_");
        builder.append(fileUtilsPort.fileNameWithoutExtension(file)).append("_");
        builder.append("cover_playlist").append(".");
        builder.append(fileUtilsPort.fileExtension(file));

        return builder.toString();
    }

    private String uploadNewCover(EditPlaylistInputPort input, String externalCoverId) {
        return fileExternalIntegrationPort.insertFile(input.getCover(), externalCoverId, FILES_IMAGES_DESTINATION, input.getCover().getContentType());
    }

    private void persistPlaylist(Playlist playlist) {
        playlistDataProviderPort.persist(playlist);
    }
}
