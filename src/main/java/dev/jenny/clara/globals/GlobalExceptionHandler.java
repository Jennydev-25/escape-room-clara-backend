package dev.jenny.clara.globals;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.jenny.clara.auth.exceptions.InvalidRefreshTokenException;
import dev.jenny.clara.contacttype.exceptions.InvalidContactTypeException;
import dev.jenny.clara.recaptcha.exceptions.InvalidRecaptchaException;
import dev.jenny.clara.register.exceptions.EmailAlreadyExistsException;
import dev.jenny.clara.user.exceptions.InvalidCurrentPasswordException;
import dev.jenny.clara.user.exceptions.UserNotFoundException;

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

    @ExceptionHandler(InvalidCurrentPasswordException.class)
    public ResponseEntity<ApiErrorDTO> handleInvalidCurrentPassword(InvalidCurrentPasswordException exception) {
        ApiErrorDTO error = new ApiErrorDTO(exception.getMessage(), HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiErrorDTO> handleInvalidRefreshToken(InvalidRefreshTokenException exception) {
        ApiErrorDTO error = new ApiErrorDTO(exception.getMessage(), HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleUserNotFound(UserNotFoundException exception) {
        ApiErrorDTO error = new ApiErrorDTO(exception.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidContactTypeException.class)
    public ResponseEntity<ApiErrorDTO> handleInvalidContactType(InvalidContactTypeException exception) {
        ApiErrorDTO error = new ApiErrorDTO(exception.getMessage(), HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}