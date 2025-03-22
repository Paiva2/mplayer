package org.com.mplayer.player.application.gateway.message;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.mplayer.player.domain.ports.in.usecase.CreateQueueUsecasePort;
import org.com.mplayer.users.SendCreateUserQueueEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class PlayerMessageController {
    private final CreateQueueUsecasePort createQueueUsecasePort;

    @ApplicationModuleListener
    public void execute(SendCreateUserQueueEvent event) {
        log.info("PlayerMessageController - New event received: SendCreateUserQueueEvent");
        
        createQueueUsecasePort.execute(event.externalUserId());
    }
}
