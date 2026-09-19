package dev.jenny.clara.common;

import java.time.LocalDateTime;

public record ApiErrorDTO(String message, int code, LocalDateTime timestamp) {
}