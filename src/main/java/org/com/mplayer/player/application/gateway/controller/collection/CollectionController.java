package org.com.mplayer.player.application.gateway.controller.collection;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.ports.in.usecase.GetOwnCollectionUsecasePort;
import org.com.mplayer.player.domain.ports.in.usecase.ListOwnCollectionsUsecasePort;
import org.com.mplayer.player.domain.ports.out.external.dto.CollectionOutput;
import org.com.mplayer.player.domain.ports.out.external.dto.GetOwnCollectionOutputPort;
import org.com.mplayer.player.domain.ports.out.utils.PageData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.com.mplayer.MplayerApplication.API_PREFIX;

@RestController
@AllArgsConstructor
@RequestMapping(API_PREFIX + "/player/collection")
public class CollectionController {
    private final ListOwnCollectionsUsecasePort listOwnCollectionsUsecasePort;
    private final GetOwnCollectionUsecasePort getOwnCollectionUsecasePort;

    @GetMapping("/mine")
    public ResponseEntity<PageData<CollectionOutput>> getMusicLyrics(
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "size", required = false, defaultValue = "5") int size
    ) {
        PageData<CollectionOutput> output = listOwnCollectionsUsecasePort.execute(
            page,
            size
        );
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @GetMapping("/{collectionId}/mine")
    public ResponseEntity<GetOwnCollectionOutputPort> getMusicLyrics(
        @PathVariable("collectionId") Long collectionId
    ) {
        GetOwnCollectionOutputPort output = getOwnCollectionUsecasePort.execute(collectionId);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }
}
