package org.com.mplayer.player.application.gateway.controller.music;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.ports.in.usecase.InsertMusicUsecasePort;
import org.com.mplayer.player.domain.ports.in.usecase.ListMusicFilesUsecasePort;
import org.com.mplayer.player.domain.ports.in.usecase.RemoveMusicUsecasePort;
import org.com.mplayer.player.domain.ports.out.external.dto.ListMusicFilesDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.com.mplayer.MplayerApplication.API_PREFIX;

@RestController
@AllArgsConstructor
@RequestMapping(API_PREFIX + "/player")
public class MusicController {
    private final InsertMusicUsecasePort insertMusicUsecasePort;
    private final RemoveMusicUsecasePort removeMusicUsecasePort;
    private final ListMusicFilesUsecasePort listMusicFilesUsecasePort;

    @PostMapping("/music/new")
    public ResponseEntity<Void> insertMusicFile(@RequestParam("file") MultipartFile file) {
        insertMusicUsecasePort.execute(file);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/music/{musicId}")
    public ResponseEntity<Void> removeMusicFile(@PathVariable("musicId") Long musicId) {
        removeMusicUsecasePort.execute(musicId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/music/list/me")
    public ResponseEntity<ListMusicFilesDTO> listMusics(
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "30") Integer size,
        @RequestParam(value = "title", required = false) String title,
        @RequestParam(value = "type", required = false) String type,
        @RequestParam(value = "artist", required = false) String artist,
        @RequestParam(value = "order", required = false) String order
    ) {
        ListMusicFilesDTO output = listMusicFilesUsecasePort.execute(page, size, title, type, artist, order);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }
}
