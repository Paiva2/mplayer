package org.com.mplayer.player.application.gateway.controller.music;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.ports.in.usecase.*;
import org.com.mplayer.player.domain.ports.out.external.dto.GetMusicDTO;
import org.com.mplayer.player.domain.ports.out.external.dto.ListMusicFilesDTO;
import org.com.mplayer.player.domain.ports.out.external.dto.StreamMusicDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;

import static org.com.mplayer.MplayerApplication.API_PREFIX;

@RestController
@AllArgsConstructor
@RequestMapping(API_PREFIX + "/player")
public class MusicController {
    private final InsertMusicUsecasePort insertMusicUsecasePort;
    private final RemoveMusicUsecasePort removeMusicUsecasePort;
    private final ListMusicFilesUsecasePort listMusicFilesUsecasePort;
    private final StreamMusicUsecasePort streamMusicUsecasePort;
    private final GetMusicUsecasePort getMusicUsecasePort;

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

    @GetMapping("/stream/music/{musicId}")
    public ResponseEntity<byte[]> streamMusic(
        @RequestHeader(value = "range", required = false) String rangeHeader,
        @PathVariable("musicId") Long musicId
    ) {
        StreamMusicDTO output = streamMusicUsecasePort.execute(musicId, rangeHeader);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(output.getMediaType()));
        headers.set("Accept-Ranges", "bytes");
        headers.setContentLength(output.getRangeLength());
        headers.set(
            "Content-Range",
            MessageFormat.format("bytes={0}-{1}/{2}", output.getStart(), output.getEnd(), output.getFileSize())
        );

        return new ResponseEntity<>(output.getContent(), headers, HttpStatus.PARTIAL_CONTENT);
    }

    @GetMapping("/music/{musicId}")
    public ResponseEntity<GetMusicDTO> streamMusic(
        @PathVariable("musicId") Long musicId
    ) {
        GetMusicDTO output = getMusicUsecasePort.execute(musicId);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

}
