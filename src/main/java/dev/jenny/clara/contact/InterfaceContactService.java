package dev.jenny.clara.contact;

import dev.jenny.clara.contact.dtos.ContactRequestDTO;
import dev.jenny.clara.contact.dtos.ContactResponseDTO;
import dev.jenny.clara.user.User;

public interface InterfaceContactService {

    ContactResponseDTO send(ContactRequestDTO request, User user);

}