package dev.jenny.clara.recaptcha.exceptions;

public class InvalidRecaptchaException extends RuntimeException {

    public InvalidRecaptchaException(String message) {
        super(message);
    }
}