package dev.jenny.clara.register.exceptions;

public class InvalidRecaptchaException extends RuntimeException {

    public InvalidRecaptchaException(String message) {
        super(message);
    }
}