package dev.jenny.clara.chat.dtos;

import java.time.LocalDateTime;

public record ChatMessageResponseDTO(Long id, String contact, String character, String content,
        LocalDateTime sentAt, int displayOrder, boolean read) {
}
