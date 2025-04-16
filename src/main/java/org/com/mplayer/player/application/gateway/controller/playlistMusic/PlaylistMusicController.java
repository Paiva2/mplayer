package org.com.mplayer.player.application.gateway.controller.playlistMusic;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.ports.in.usecase.InsertPlaylistMusicUsecasePort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.com.mplayer.MplayerApplication.API_PREFIX;

@RestController
@AllArgsConstructor
@RequestMapping(API_PREFIX + "/player/playlist-music")
public class PlaylistMusicController {
    private final InsertPlaylistMusicUsecasePort insertPlaylistMusicUsecasePort;

    @PostMapping("/insert/playlist/{playlistId}/music/{musicId}")
    @Transactional
    public ResponseEntity<Void> insert(
        @PathVariable("playlistId") Long playlistId,
        @PathVariable("musicId") Long musicId
    ) {
        insertPlaylistMusicUsecasePort.execute(playlistId, musicId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
