package org.com.mplayer.player.application.gateway.controller.musicQueue;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.ports.in.external.RemoveMusicQueueInputPort;
import org.com.mplayer.player.domain.ports.in.usecase.ChangeMusicQueuePositionUsecasePort;
import org.com.mplayer.player.domain.ports.in.usecase.InsertMusicQueueUsecasePort;
import org.com.mplayer.player.domain.ports.in.usecase.ListQueueMusicsUsecasePort;
import org.com.mplayer.player.domain.ports.in.usecase.RemoveMusicQueueUsecasePort;
import org.com.mplayer.player.domain.ports.out.external.dto.ListQueueMusicsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.com.mplayer.MplayerApplication.API_PREFIX;

@RestController
@AllArgsConstructor
@RequestMapping(API_PREFIX + "/player")
public class MusicQueueController {
    private final ListQueueMusicsUsecasePort listQueueMusicsUsecasePort;
    private final InsertMusicQueueUsecasePort insertMusicQueueUsecasePort;
    private final RemoveMusicQueueUsecasePort removeMusicQueueUsecasePort;
    private final ChangeMusicQueuePositionUsecasePort changeMusicQueuePositionUsecasePort;

    @GetMapping("/music-queue/list")
    public ResponseEntity<ListQueueMusicsDTO> listQueue() {
        ListQueueMusicsDTO output = listQueueMusicsUsecasePort.execute();
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @PostMapping("/music-queue/insert/music/{musicId}")
    public ResponseEntity<Void> insertMusicQueue(
        @PathVariable("musicId") Long musicId
    ) {
        insertMusicQueueUsecasePort.execute(musicId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/music-queue/remove/music/{musicId}/position/{position}")
    public ResponseEntity<Void> removeMusicQueue(
        @PathVariable("musicId") Long musicId,
        @PathVariable("position") Integer position
    ) {
        removeMusicQueueUsecasePort.execute(RemoveMusicQueueInputPort.builder()
            .musicId(musicId)
            .position(position)
            .build()
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/music-queue/music/{musicId}/position/{position}/new/{newPosition}")
    public ResponseEntity<Void> updateMusicQueuePosition(
        @PathVariable("musicId") Long musicId,
        @PathVariable("position") Integer position,
        @PathVariable("newPosition") Integer newPosition
    ) {
        changeMusicQueuePositionUsecasePort.execute(musicId, position, newPosition);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}