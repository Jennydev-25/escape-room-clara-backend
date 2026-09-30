package dev.jenny.clara.contact;

import dev.jenny.clara.contact.dtos.ContactRequestDTO;
import dev.jenny.clara.contact.dtos.ContactResponseDTO;

public interface InterfaceContactService {

    ContactResponseDTO send(ContactRequestDTO request);

}