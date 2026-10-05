package dev.jenny.clara.contact.mappers;

import java.time.LocalDateTime;

import dev.jenny.clara.contact.ContactMessageEntity;
import dev.jenny.clara.contact.dtos.ContactRequestDTO;
import dev.jenny.clara.user.UserEntity;

public class ContactMessageMapper {

    private ContactMessageMapper() {
        throw new UnsupportedOperationException("No se puede instanciar esta clase de utilidad");
    }

    public static ContactMessageEntity toEntity(ContactRequestDTO request, UserEntity user) {
        return ContactMessageEntity.builder()
                .name(request.name())
                .email(request.email())
                .type(request.type())
                .message(request.message())
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
