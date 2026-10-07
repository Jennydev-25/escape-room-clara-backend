package dev.jenny.clara.refreshtoken.dtos;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDTO(
        @NotBlank(message = "El refresh token es obligatorio") String refreshToken) {
}
