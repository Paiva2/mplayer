package org.com.mplayer.player.application.gateway.controller.music;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.ports.in.usecase.InsertMusicUsecasePort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.com.mplayer.MplayerApplication.API_PREFIX;

@RestController
@AllArgsConstructor
@RequestMapping(API_PREFIX + "/player")
public class MusicController {
    private final InsertMusicUsecasePort insertMusicUsecasePort;

    @PostMapping("/music/new")
    public ResponseEntity<Void> insertMusicFile(@RequestParam("file") MultipartFile file) {
        insertMusicUsecasePort.execute(file);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
