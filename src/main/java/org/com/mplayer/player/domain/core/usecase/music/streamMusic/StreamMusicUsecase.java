package org.com.mplayer.player.domain.core.usecase.music.streamMusic;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Music;
import org.com.mplayer.player.domain.core.enums.EFileType;
import org.com.mplayer.player.domain.core.usecase.common.exception.MusicNotBelongUserException;
import org.com.mplayer.player.domain.core.usecase.common.exception.MusicNotFoundException;
import org.com.mplayer.player.domain.ports.in.usecase.StreamMusicUsecasePort;
import org.com.mplayer.player.domain.ports.out.data.MusicDataProviderPort;
import org.com.mplayer.player.domain.ports.out.external.FileExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.UserExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileDTO;
import org.com.mplayer.player.domain.ports.out.external.dto.StreamMusicDTO;
import org.com.mplayer.player.infra.annotations.Usecase;

import java.util.Map;

@Usecase
@AllArgsConstructor
public class StreamMusicUsecase implements StreamMusicUsecasePort {
    private final static String FILES_TRACKS_DESTINATION = "mplayer-tracks";

    private final UserExternalIntegrationPort userExternalIntegrationPort;
    private final MusicDataProviderPort musicDataProviderPort;
    private final FileExternalIntegrationPort fileExternalIntegrationPort;

    public StreamMusicDTO execute(Long musicId, String byteRange) {
        FindUserExternalProfileDTO user = findUser();

        Music music = findMusic(musicId);

        checkMusicUser(user, music);

        String externalMusicId = music.getExternalIdentification().concat("_track.").concat(music.getFileType().getGetTypeLower());
        Map<String, Object> stream = fileExternalIntegrationPort.streamFile(FILES_TRACKS_DESTINATION, externalMusicId, byteRange);

        return mountOutput(stream, defineMediaType(music.getFileType()));
    }

    private FindUserExternalProfileDTO findUser() {
        return userExternalIntegrationPort.findByExternalId();
    }

    private Music findMusic(Long musicId) {
        return musicDataProviderPort.findByIdWithDeps(musicId).orElseThrow(MusicNotFoundException::new);
    }

    private void checkMusicUser(FindUserExternalProfileDTO user, Music music) {
        if (!music.getExternalUserId().equals(user.getId().toString())) {
            throw new MusicNotBelongUserException();
        }
    }

    private String defineMediaType(EFileType fileType) {
        String mediaType = null;

        switch (fileType) {
            case MP3 -> mediaType = "audio/mpeg";
            case FLAC -> mediaType = "audio/flac";
        }

        return mediaType;
    }

    private StreamMusicDTO mountOutput(Map<String, Object> stream, String mediaType) {
        return StreamMusicDTO.builder()
            .content((byte[]) stream.get("content"))
            .rangeLength((long) stream.get("rangeLength"))
            .end((long) stream.get("end"))
            .start((long) stream.get("start"))
            .fileSize((long) stream.get("fileSize"))
            .mediaType(mediaType)
            .build();
    }
}
