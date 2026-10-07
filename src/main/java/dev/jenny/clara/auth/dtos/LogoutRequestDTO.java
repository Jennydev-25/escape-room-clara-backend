package dev.jenny.clara.auth.dtos;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequestDTO(
        @NotBlank(message = "El refresh token es obligatorio") String refreshToken) {
}