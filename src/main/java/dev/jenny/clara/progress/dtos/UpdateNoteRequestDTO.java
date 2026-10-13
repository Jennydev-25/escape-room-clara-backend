package dev.jenny.clara.progress.dtos;

import jakarta.validation.constraints.NotNull;

public record UpdateNoteRequestDTO(
        @NotNull(message = "La nota no puede ser nula")
        String freeNote) {
}
