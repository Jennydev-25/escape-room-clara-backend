package dev.jenny.clara.email;

import java.util.List;

import org.springframework.security.core.Authentication;

import dev.jenny.clara.email.dtos.EmailDetailDTO;
import dev.jenny.clara.email.dtos.EmailSummaryDTO;

public interface InterfaceEmailService {

    List<EmailSummaryDTO> listInbox(Authentication authentication);

    EmailDetailDTO getDetail(Authentication authentication, Long emailId);
}
