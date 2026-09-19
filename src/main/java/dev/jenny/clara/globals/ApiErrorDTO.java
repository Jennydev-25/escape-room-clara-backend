package dev.jenny.clara.globals;

import java.time.LocalDateTime;

public record ApiErrorDTO(String message, int code, LocalDateTime timestamp) {
}