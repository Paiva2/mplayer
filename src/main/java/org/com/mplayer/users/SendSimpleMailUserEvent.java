package org.com.mplayer.users;

import org.springframework.modulith.events.Externalized;

import static org.com.mplayer.global.QueueConstants.MAIL_DESTINATION;

@Externalized(MAIL_DESTINATION)
public record SendSimpleMailUserEvent(String to, String subject, String content) {

}
