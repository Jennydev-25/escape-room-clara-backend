package dev.jenny.clara.email.mappers;

import dev.jenny.clara.email.EmailEntity;
import dev.jenny.clara.email.dtos.EmailDetailDTO;
import dev.jenny.clara.email.dtos.EmailSummaryDTO;

public class EmailMapper {

    private EmailMapper() {
        throw new UnsupportedOperationException("No se puede instanciar esta clase de utilidad");
    }

    public static EmailSummaryDTO toSummaryDTO(EmailEntity email, boolean read) {
        return new EmailSummaryDTO(email.getId(), email.getSender(), email.getSubject(), read);
    }

    public static EmailDetailDTO toDetailDTO(EmailEntity email) {
        return new EmailDetailDTO(email.getId(), email.getSender(), email.getRecipient(), email.getSubject(),
                email.getSentDate(), email.getBody());
    }
}
