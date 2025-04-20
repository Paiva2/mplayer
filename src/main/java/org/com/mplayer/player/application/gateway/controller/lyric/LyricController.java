package org.com.mplayer.player.application.gateway.controller.lyric;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.ports.in.usecase.GetMusicLyricsUsecasePort;
import org.com.mplayer.player.domain.ports.out.external.dto.GetMusicLyricsOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.com.mplayer.MplayerApplication.API_PREFIX;

@RestController
@AllArgsConstructor
@RequestMapping(API_PREFIX + "/player/lyric")
public class LyricController {
    private final GetMusicLyricsUsecasePort getMusicLyricsUsecasePort;

    @GetMapping("/music/{musicId}")
    public ResponseEntity<GetMusicLyricsOutput> getMusicLyrics(
        @PathVariable("musicId") Long musicId
    ) {
        GetMusicLyricsOutput output = getMusicLyricsUsecasePort.execute(musicId);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }
}
