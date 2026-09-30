package dev.jenny.clara.contact;

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
        throw new UnsupportedOperationException("Not implemented yet");
    }
}