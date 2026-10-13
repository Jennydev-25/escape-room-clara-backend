package dev.jenny.clara.progress.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AccumulateTimeRequestDTO(
        @NotNull(message = "Los segundos a acumular son obligatorios")
        @Positive(message = "Los segundos a acumular deben ser un valor positivo")
        Long secondsToAdd) {
}
