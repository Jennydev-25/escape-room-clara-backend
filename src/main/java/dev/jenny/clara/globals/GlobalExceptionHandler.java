package dev.jenny.clara.globals;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.jenny.clara.recaptcha.exceptions.InvalidRecaptchaException;
import dev.jenny.clara.register.exceptions.EmailAlreadyExistsException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorDTO> handleEmailAlreadyExists(EmailAlreadyExistsException exception) {
        ApiErrorDTO error = new ApiErrorDTO(exception.getMessage(), HttpStatus.CONFLICT.value(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(InvalidRecaptchaException.class)
    public ResponseEntity<ApiErrorDTO> handleInvalidRecaptcha(InvalidRecaptchaException exception) {
        ApiErrorDTO error = new ApiErrorDTO(exception.getMessage(), HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}