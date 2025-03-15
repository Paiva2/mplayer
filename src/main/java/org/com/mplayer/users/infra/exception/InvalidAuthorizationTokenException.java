package org.com.mplayer.users.infra.exception;

public class InvalidAuthorizationTokenException extends RuntimeException {
    public InvalidAuthorizationTokenException(String message) {
        super(message);
    }
}
