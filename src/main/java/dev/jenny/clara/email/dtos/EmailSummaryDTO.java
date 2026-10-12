package dev.jenny.clara.email.dtos;

import java.time.LocalDate;

public record EmailSummaryDTO(Long id, String sender, String subject, LocalDate sentDate, boolean read) {
}
