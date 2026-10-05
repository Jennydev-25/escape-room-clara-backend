package dev.jenny.clara.user.dtos;

public record UserProfileResponseDTO(
        String alias,
        String email,
        Integer avatarId) {
}