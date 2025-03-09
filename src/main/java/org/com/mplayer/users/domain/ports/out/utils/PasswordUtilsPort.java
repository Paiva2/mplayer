package org.com.mplayer.users.domain.ports.out.utils;

public interface PasswordUtilsPort {
    String hashPassword(String rawPassword);

    boolean comparePassword(String rawPassword, String hashedPassword);
}
