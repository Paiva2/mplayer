package org.com.mplayer.users;

import org.springframework.modulith.events.Externalized;

import static org.com.mplayer.users.infra.config.constants.QueueConstants.MAIL_EXCHANGE;

@Externalized(target = MAIL_EXCHANGE)
public record SendSimpleMailUserEvent(String to, String subject, String content) {

}
