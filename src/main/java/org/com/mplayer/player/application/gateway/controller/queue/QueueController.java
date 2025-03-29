package org.com.mplayer.player.application.gateway.controller.queue;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.ports.in.usecase.ListQueueMusicsUsecasePort;
import org.com.mplayer.player.domain.ports.out.external.dto.ListQueueMusicsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.com.mplayer.MplayerApplication.API_PREFIX;

@RestController
@AllArgsConstructor
@RequestMapping(API_PREFIX + "/player")
public class QueueController {
    private final ListQueueMusicsUsecasePort listQueueMusicsUsecasePort;

    @GetMapping("/queue/list")
    public ResponseEntity<ListQueueMusicsDTO> listQueue() {
        ListQueueMusicsDTO output = listQueueMusicsUsecasePort.execute();
        return new ResponseEntity<>(output, HttpStatus.OK);
    }
}
