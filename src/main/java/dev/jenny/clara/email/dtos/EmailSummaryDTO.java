package dev.jenny.clara.email.dtos;

public record EmailSummaryDTO(Long id, String sender, String subject, boolean read) {
}
