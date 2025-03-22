package org.com.mplayer.users.domain.ports.out.event;

public interface UserSendApplicationEventPort {
    void send(Object event);
}
