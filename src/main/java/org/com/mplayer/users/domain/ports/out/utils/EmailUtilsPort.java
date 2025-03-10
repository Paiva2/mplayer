package org.com.mplayer.users.domain.ports.out.utils;

public interface EmailUtilsPort {
    boolean validateEmailFormat(String email);

    void sendSimpleMail(String to, String subject, String content);
}
