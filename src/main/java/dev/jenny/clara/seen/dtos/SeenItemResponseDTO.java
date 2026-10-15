package dev.jenny.clara.seen.dtos;

import java.time.LocalDateTime;

public record SeenItemResponseDTO(
        String key,
        LocalDateTime seenAt) {
}
