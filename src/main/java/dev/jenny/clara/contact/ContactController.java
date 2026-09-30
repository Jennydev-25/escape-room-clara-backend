package dev.jenny.clara.contact;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jenny.clara.contact.dtos.ContactRequestDTO;
import dev.jenny.clara.contact.dtos.ContactResponseDTO;
import dev.jenny.clara.security.SecurityUser;
import dev.jenny.clara.user.User;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "${api-endpoint}/contact")
public class ContactController {

    private final InterfaceContactService service;

    public ContactController(InterfaceContactService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ContactResponseDTO> send(@Valid @RequestBody ContactRequestDTO dto,
            @AuthenticationPrincipal SecurityUser securityUser) {
        User user = securityUser != null ? securityUser.getUser() : null;
        ContactResponseDTO responseDto = service.send(dto, user);
        return ResponseEntity.status(201).body(responseDto);
    }
}