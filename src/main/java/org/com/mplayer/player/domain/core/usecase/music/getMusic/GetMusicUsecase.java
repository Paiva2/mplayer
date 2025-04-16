package org.com.mplayer.player.domain.core.usecase.music.getMusic;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Music;
import org.com.mplayer.player.domain.core.usecase.common.exception.MusicNotBelongUserException;
import org.com.mplayer.player.domain.core.usecase.common.exception.MusicNotFoundException;
import org.com.mplayer.player.domain.ports.in.usecase.GetMusicUsecasePort;
import org.com.mplayer.player.domain.ports.out.data.MusicDataProviderPort;
import org.com.mplayer.player.domain.ports.out.external.UserExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileOutputPort;
import org.com.mplayer.player.domain.ports.out.external.dto.GetMusicOutputPort;
import org.com.mplayer.player.infra.annotations.Usecase;

@Usecase
@AllArgsConstructor
public class GetMusicUsecase implements GetMusicUsecasePort {
    private final UserExternalIntegrationPort userExternalIntegrationPort;
    private final MusicDataProviderPort musicDataProviderPort;

    @Override
    public GetMusicOutputPort execute(Long musicId) {
        FindUserExternalProfileOutputPort user = findUser();
        Music music = findMusic(musicId);

        checkMusicUser(user, music);

        return mountOutput(music);
    }

    private FindUserExternalProfileOutputPort findUser() {
        return userExternalIntegrationPort.findByExternalId();
    }

    private Music findMusic(Long musicId) {
        return musicDataProviderPort.findByIdWithDeps(musicId).orElseThrow(MusicNotFoundException::new);
    }

    private void checkMusicUser(FindUserExternalProfileOutputPort user, Music music) {
        if (!music.getExternalUserId().equals(user.getId().toString())) {
            throw new MusicNotBelongUserException();
        }
    }

    private GetMusicOutputPort mountOutput(Music music) {
        return new GetMusicOutputPort(music);
    }
}
