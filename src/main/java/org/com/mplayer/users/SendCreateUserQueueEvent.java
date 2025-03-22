package org.com.mplayer.users;

import org.springframework.modulith.events.Externalized;

import static org.com.mplayer.QueueConstants.MAIL_EXCHANGE;

@Externalized(target = MAIL_EXCHANGE)
public record SendCreateUserQueueEvent(String externalUserId) {
}
