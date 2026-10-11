package dev.jenny.clara.chat.dtos;

public record ChatMessageResponseDTO(Long id, String contact, String character, String content, int displayOrder,
        boolean read) {
}
