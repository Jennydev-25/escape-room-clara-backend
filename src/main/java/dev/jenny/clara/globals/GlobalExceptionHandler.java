package dev.jenny.clara.globals;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.jenny.clara.chat.exceptions.ContactNotFoundException;
import dev.jenny.clara.contacttype.exceptions.InvalidContactTypeException;
import dev.jenny.clara.email.exceptions.EmailChapterNotReachedException;
import dev.jenny.clara.email.exceptions.EmailNotFoundException;
import dev.jenny.clara.folder.exceptions.ChapterNotReachedException;
import dev.jenny.clara.folder.exceptions.FolderNotFoundException;
import dev.jenny.clara.puzzle.exceptions.HintNotFoundException;
import dev.jenny.clara.puzzle.exceptions.PuzzleChapterNotReachedException;
import dev.jenny.clara.puzzle.exceptions.PuzzleNotFoundException;
import dev.jenny.clara.puzzle.exceptions.UnsupportedPuzzleTypeException;
import dev.jenny.clara.recaptcha.exceptions.InvalidRecaptchaException;
import dev.jenny.clara.refreshtoken.exceptions.InvalidRefreshTokenException;
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

    @ExceptionHandler(FolderNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleFolderNotFound(FolderNotFoundException exception) {
        ApiErrorDTO error = new ApiErrorDTO(exception.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ChapterNotReachedException.class)
    public ResponseEntity<ApiErrorDTO> handleChapterNotReached(ChapterNotReachedException exception) {
        ApiErrorDTO error = new ApiErrorDTO(exception.getMessage(), HttpStatus.FORBIDDEN.value(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleEmailNotFound(EmailNotFoundException exception) {
        ApiErrorDTO error = new ApiErrorDTO(exception.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EmailChapterNotReachedException.class)
    public ResponseEntity<ApiErrorDTO> handleEmailChapterNotReached(EmailChapterNotReachedException exception) {
        ApiErrorDTO error = new ApiErrorDTO(exception.getMessage(), HttpStatus.FORBIDDEN.value(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(ContactNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleContactNotFound(ContactNotFoundException exception) {
        ApiErrorDTO error = new ApiErrorDTO(exception.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(PuzzleNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handlePuzzleNotFound(PuzzleNotFoundException exception) {
        ApiErrorDTO error = new ApiErrorDTO(exception.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(HintNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleHintNotFound(HintNotFoundException exception) {
        ApiErrorDTO error = new ApiErrorDTO(exception.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(PuzzleChapterNotReachedException.class)
    public ResponseEntity<ApiErrorDTO> handlePuzzleChapterNotReached(PuzzleChapterNotReachedException exception) {
        ApiErrorDTO error = new ApiErrorDTO(exception.getMessage(), HttpStatus.FORBIDDEN.value(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(UnsupportedPuzzleTypeException.class)
    public ResponseEntity<ApiErrorDTO> handleUnsupportedPuzzleType(UnsupportedPuzzleTypeException exception) {
        ApiErrorDTO error = new ApiErrorDTO(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
