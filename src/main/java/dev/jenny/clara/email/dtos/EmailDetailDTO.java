package dev.jenny.clara.email.dtos;

import java.time.LocalDate;

public record EmailDetailDTO(Long id, String sender, String recipient, String subject, LocalDate sentDate,
        String body) {
}
