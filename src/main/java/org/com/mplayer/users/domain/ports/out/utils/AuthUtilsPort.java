package org.com.mplayer.users.domain.ports.out.utils;

public interface AuthUtilsPort {
    String generate(String subject);

    String verify(String token, String claim);
}
