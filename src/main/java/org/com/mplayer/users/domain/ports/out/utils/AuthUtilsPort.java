package org.com.mplayer.users.domain.ports.out.utils;

public interface AuthUtilsPort {
    String generate(String subject);

    void verify(String token);
}
