package dev.jenny.clara.email;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jenny.clara.email.dtos.EmailDetailDTO;
import dev.jenny.clara.email.dtos.EmailSummaryDTO;

@RestController
@RequestMapping(path = "${api-endpoint}/emails")
public class EmailController {

    private final InterfaceEmailService emailService;

    public EmailController(InterfaceEmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping
    public List<EmailSummaryDTO> listInbox(Authentication authentication) {
        return emailService.listInbox(authentication);
    }

    @GetMapping("/{id}")
    public EmailDetailDTO getEmail(Authentication authentication, @PathVariable Long id) {
        return emailService.getDetail(authentication, id);
    }
}
