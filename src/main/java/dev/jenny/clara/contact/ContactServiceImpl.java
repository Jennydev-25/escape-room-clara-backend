package dev.jenny.clara.contact;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import dev.jenny.clara.contact.dtos.ContactRequestDTO;
import dev.jenny.clara.contact.dtos.ContactResponseDTO;
import dev.jenny.clara.recaptcha.RecaptchaService;
import dev.jenny.clara.recaptcha.exceptions.InvalidRecaptchaException;

@Service
public class ContactServiceImpl implements InterfaceContactService {

    private final ContactRepository contactRepository;
    private final RecaptchaService recaptchaService;

    public ContactServiceImpl(ContactRepository contactRepository, RecaptchaService recaptchaService) {
        this.contactRepository = contactRepository;
        this.recaptchaService = recaptchaService;
    }

    @Override
    public ContactResponseDTO send(ContactRequestDTO request) {
        if (!recaptchaService.verify(request.recaptchaToken())) {
            throw new InvalidRecaptchaException("Invalid recaptcha token.");
        }

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