package org.com.mplayer.player.application.gateway.controller.playlistMusic;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.ports.in.usecase.InsertPlaylistMusicUsecasePort;
import org.com.mplayer.player.domain.ports.in.usecase.RemovePlaylistMusicUsecasePort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.com.mplayer.MplayerApplication.API_PREFIX;

@RestController
@AllArgsConstructor
@RequestMapping(API_PREFIX + "/player/playlist-music")
public class PlaylistMusicController {
    private final InsertPlaylistMusicUsecasePort insertPlaylistMusicUsecasePort;
    private final RemovePlaylistMusicUsecasePort removePlaylistMusicUsecasePort;

    @Transactional
    @PostMapping("/insert/playlist/{playlistId}/music/{musicId}")
    public ResponseEntity<Void> insert(
        @PathVariable("playlistId") Long playlistId,
        @PathVariable("musicId") Long musicId
    ) {
        insertPlaylistMusicUsecasePort.execute(playlistId, musicId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @Transactional
    @DeleteMapping("/remove/playlist/{playlistId}/music/{musicId}")
    public ResponseEntity<Void> delete(
        @PathVariable("playlistId") Long playlistId,
        @PathVariable("musicId") Long musicId
    ) {
        removePlaylistMusicUsecasePort.execute(playlistId, musicId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
