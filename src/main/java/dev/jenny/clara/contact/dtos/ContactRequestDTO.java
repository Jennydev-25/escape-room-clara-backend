package dev.jenny.clara.contact.dtos;

import dev.jenny.clara.contact.ContactType;

public record ContactRequestDTO(String name, String email, ContactType type, String message) {
}