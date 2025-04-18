package org.com.mplayer.player.application.gateway.controller.playlist;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.ports.in.external.CreatePlaylistInputPort;
import org.com.mplayer.player.domain.ports.in.external.EditPlaylistInputPort;
import org.com.mplayer.player.domain.ports.in.usecase.*;
import org.com.mplayer.player.domain.ports.out.external.dto.GetOwnPlaylistOutputPort;
import org.com.mplayer.player.domain.ports.out.external.dto.GetUserPlaylistOutputPort;
import org.com.mplayer.player.domain.ports.out.external.dto.ListUserPlaylistsOutputPort;
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
    private final EditPlaylistUsecasePort editPlaylistUsecasePort;
    private final ListOwnPlaylistsUsecasePort listOwnPlaylistsUsecasePort;
    private final DeletePlaylistUsecasePort deletePlaylistUsecasePort;
    private final GetOwnPlaylistUsecasePort getOwnPlaylistUsecasePort;
    private final ListUserPlaylistsUsecasePort listUserPlaylistsUsecasePort;
    private final GetUserPlaylistUsecasePort getUserPlaylistUsecasePort;

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

    @GetMapping("/list/user/{userId}")
    public ResponseEntity<PageData<ListUserPlaylistsOutputPort>> getUserPlaylist(
        @PathVariable("userId") String userId,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "50") Integer size,
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "direction", required = false, defaultValue = "desc") String direction
    ) {
        PageData<ListUserPlaylistsOutputPort> output = listUserPlaylistsUsecasePort.execute(
            userId,
            page,
            size,
            name,
            direction
        );
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @GetMapping("/{playlistId}/user/{userId}")
    public ResponseEntity<GetUserPlaylistOutputPort> getUserPlaylist(
        @PathVariable("playlistId") long playlistId,
        @PathVariable("userId") String userId,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "50") Integer size
    ) {
        GetUserPlaylistOutputPort output = getUserPlaylistUsecasePort.execute(userId, playlistId, page, size);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @PatchMapping("/{playlistId}/edit")
    public ResponseEntity<Void> editPlaylist(
        @PathVariable("playlistId") Long playlistId,
        @RequestParam(name = "file", required = false) MultipartFile file,
        @RequestParam(name = "playlist-name", required = false) String playlistName,
        @RequestParam(name = "visible-public", required = false) Boolean visiblePublic
    ) {
        editPlaylistUsecasePort.execute(playlistId, EditPlaylistInputPort.builder()
            .cover(file)
            .name(playlistName)
            .visiblePublic(visiblePublic)
            .build()
        );
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
