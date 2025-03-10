package org.com.mplayer.users.infra.adapters.utils;

import lombok.AllArgsConstructor;
import org.com.mplayer.users.SendSimpleMailUserEvent;
import org.com.mplayer.users.domain.ports.out.utils.EmailUtilsPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
public class EmailUtilsAdapter implements EmailUtilsPort {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public boolean validateEmailFormat(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.matches();
    }

    @Override
    public void sendSimpleMail(String to, String subject, String content) {
        applicationEventPublisher.publishEvent(new SendSimpleMailUserEvent(to, subject, content));
    }
}
