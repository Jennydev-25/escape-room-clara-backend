package dev.jenny.clara.email.exceptions;

public class EmailNotFoundException extends RuntimeException {

    public EmailNotFoundException(String message) {
        super(message);
    }
}
