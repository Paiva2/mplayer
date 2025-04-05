package org.com.mplayer.player.application.gateway.controller.queue;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.com.mplayer.MplayerApplication.API_PREFIX;

@RestController
@AllArgsConstructor
@RequestMapping(API_PREFIX + "/player")
public class QueueController {
}
