package org.com.mplayer.player.domain.core.usecase.music.getMusic;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Music;
import org.com.mplayer.player.domain.core.usecase.common.exception.MusicNotBelongUserException;
import org.com.mplayer.player.domain.core.usecase.common.exception.MusicNotFoundException;
import org.com.mplayer.player.domain.ports.in.usecase.GetMusicUsecasePort;
import org.com.mplayer.player.domain.ports.out.data.MusicDataProviderPort;
import org.com.mplayer.player.domain.ports.out.external.UserExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileDTO;
import org.com.mplayer.player.domain.ports.out.external.dto.GetMusicDTO;
import org.com.mplayer.player.infra.annotations.Usecase;

@Usecase
@AllArgsConstructor
public class GetMusicUsecase implements GetMusicUsecasePort {
    private final UserExternalIntegrationPort userExternalIntegrationPort;
    private final MusicDataProviderPort musicDataProviderPort;

    @Override
    public GetMusicDTO execute(Long musicId) {
        FindUserExternalProfileDTO user = findUser();
        Music music = findMusic(musicId);

        checkMusicUser(user, music);

        return mountOutput(music);
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

    private GetMusicDTO mountOutput(Music music) {
        return new GetMusicDTO(music);
    }
}
