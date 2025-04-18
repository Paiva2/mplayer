package org.com.mplayer.player.application.gateway.controller.playlist;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.ports.in.external.CreatePlaylistInputPort;
import org.com.mplayer.player.domain.ports.in.usecase.CreatePlaylistUsecasePort;
import org.com.mplayer.player.domain.ports.in.usecase.DeletePlaylistUsecasePort;
import org.com.mplayer.player.domain.ports.in.usecase.GetOwnPlaylistUsecasePort;
import org.com.mplayer.player.domain.ports.in.usecase.ListOwnPlaylistsUsecasePort;
import org.com.mplayer.player.domain.ports.out.external.dto.GetOwnPlaylistOutputPort;
import org.com.mplayer.player.domain.ports.out.external.dto.PlaylistOutputPort;
import org.com.mplayer.player.domain.ports.out.utils.PageData;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.com.mplayer.MplayerApplication.API_PREFIX;

@RestController
@AllArgsConstructor
@RequestMapping(API_PREFIX + "/player/playlist")
public class PlaylistController {
    private final CreatePlaylistUsecasePort createPlaylistUsecasePort;
    private final ListOwnPlaylistsUsecasePort listOwnPlaylistsUsecasePort;
    private final DeletePlaylistUsecasePort deletePlaylistUsecasePort;
    private final GetOwnPlaylistUsecasePort getOwnPlaylistUsecasePort;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> create(
        @RequestParam(name = "file", required = false) MultipartFile file,
        @RequestParam(name = "playlist-name", required = true) String playlistName,
        @RequestParam(name = "visible-public", required = true) Boolean visiblePublic
    ) {
        createPlaylistUsecasePort.execute(CreatePlaylistInputPort.builder()
            .name(playlistName)
            .visiblePublic(visiblePublic)
            .cover(file)
            .build()
        );

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/list/mine")
    public ResponseEntity<PageData<PlaylistOutputPort>> list(
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "5") Integer size,
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "direction", required = false) String direction
    ) {
        PageData<PlaylistOutputPort> playlists = listOwnPlaylistsUsecasePort.execute(
            page,
            size,
            name,
            direction
        );
        return new ResponseEntity<>(playlists, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/{id}/remove")
    public ResponseEntity<Void> remove(@PathVariable("id") long id) {
        deletePlaylistUsecasePort.execute(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{playlistId}/mine")
    public ResponseEntity<GetOwnPlaylistOutputPort> getOwnPlaylist(
        @PathVariable("playlistId") long playlistId,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "50") Integer size
    ) {
        GetOwnPlaylistOutputPort output = getOwnPlaylistUsecasePort.execute(playlistId, page, size);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }
}
