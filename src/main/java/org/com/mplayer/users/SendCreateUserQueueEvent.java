package org.com.mplayer.users;

import org.springframework.modulith.events.Externalized;

import static org.com.mplayer.global.QueueConstants.PLAYER_DESTINATION;

@Externalized(target = PLAYER_DESTINATION)
public record SendCreateUserQueueEvent(String externalUserId) {
}
