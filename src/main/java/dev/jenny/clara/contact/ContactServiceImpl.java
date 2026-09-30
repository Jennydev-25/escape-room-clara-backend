package dev.jenny.clara.contact;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import dev.jenny.clara.contact.dtos.ContactRequestDTO;
import dev.jenny.clara.contact.dtos.ContactResponseDTO;

@Service
public class ContactServiceImpl implements InterfaceContactService {

    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public ContactResponseDTO send(ContactRequestDTO request) {
        ContactMessage contactMessage = ContactMessage.builder()
                .name(request.name())
                .email(request.email())
                .type(request.type())
                .message(request.message())
                .createdAt(LocalDateTime.now())
                .build();

        contactRepository.save(contactMessage);

        return new ContactResponseDTO("Mensaje recibido correctamente. Tendrás respuesta en menos de 24/48 horas.");
    }
}