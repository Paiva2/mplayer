package org.com.mplayer.users.infra.adapters.event;

import lombok.AllArgsConstructor;
import org.com.mplayer.users.domain.ports.out.event.UserSendApplicationEventPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserSendApplicationEventAdapter implements UserSendApplicationEventPort {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void send(Object event) {
        applicationEventPublisher.publishEvent(event);
    }
}
