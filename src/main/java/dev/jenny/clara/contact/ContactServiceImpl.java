package dev.jenny.clara.contact;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import dev.jenny.clara.contact.dtos.ContactRequestDTO;
import dev.jenny.clara.contact.dtos.ContactResponseDTO;
import dev.jenny.clara.recaptcha.RecaptchaService;
import dev.jenny.clara.user.UserEntity;

@Service
public class ContactServiceImpl implements InterfaceContactService {

    private final ContactRepository contactRepository;
    private final RecaptchaService recaptchaService;

    public ContactServiceImpl(ContactRepository contactRepository, RecaptchaService recaptchaService) {
        this.contactRepository = contactRepository;
        this.recaptchaService = recaptchaService;
    }

    @Override
    public ContactResponseDTO send(ContactRequestDTO request, UserEntity user) {
        recaptchaService.verifyOrThrow(request.recaptchaToken());

        ContactMessageEntity contactMessage = ContactMessageEntity.builder()
                .name(request.name())
                .email(request.email())
                .type(request.type())
                .message(request.message())
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        contactRepository.save(contactMessage);

        return new ContactResponseDTO("Mensaje recibido correctamente. Tendrás respuesta en menos de 24/48 horas.");
    }
}