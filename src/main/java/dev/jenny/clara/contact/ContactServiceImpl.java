package dev.jenny.clara.contact;

import org.springframework.stereotype.Service;

import dev.jenny.clara.contact.dtos.ContactRequestDTO;
import dev.jenny.clara.contact.dtos.ContactResponseDTO;
import dev.jenny.clara.contact.mappers.ContactMessageMapper;
import dev.jenny.clara.contacttype.ContactTypeEntity;
import dev.jenny.clara.contacttype.ContactTypeRepository;
import dev.jenny.clara.contacttype.exceptions.InvalidContactTypeException;
import dev.jenny.clara.recaptcha.RecaptchaService;
import dev.jenny.clara.user.UserEntity;

@Service
public class ContactServiceImpl implements InterfaceContactService {

    private final ContactRepository contactRepository;
    private final RecaptchaService recaptchaService;
    private final ContactTypeRepository contactTypeRepository;

    public ContactServiceImpl(ContactRepository contactRepository, RecaptchaService recaptchaService,
            ContactTypeRepository contactTypeRepository) {
        this.contactRepository = contactRepository;
        this.recaptchaService = recaptchaService;
        this.contactTypeRepository = contactTypeRepository;
    }

    @Override
    public ContactResponseDTO send(ContactRequestDTO request, UserEntity user) {
        recaptchaService.verifyOrThrow(request.recaptchaToken());

        ContactTypeEntity type = contactTypeRepository.findByName(request.type())
                .orElseThrow(() -> new InvalidContactTypeException(
                        "El tipo de contacto '" + request.type() + "' no es válido."));

        ContactMessageEntity contactMessage = ContactMessageMapper.toEntity(request, user, type);

        contactRepository.save(contactMessage);

        return new ContactResponseDTO("Mensaje recibido correctamente. Tendrás respuesta en menos de 24/48 horas.");
    }
}
