package dev.jenny.clara.refreshtoken.exceptions;

public class InvalidRefreshTokenException extends RuntimeException {

    public InvalidRefreshTokenException(String message) {
        super(message);
    }

}
