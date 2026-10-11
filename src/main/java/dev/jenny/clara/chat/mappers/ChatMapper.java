package dev.jenny.clara.chat.mappers;

import dev.jenny.clara.chat.ChatMessageEntity;
import dev.jenny.clara.chat.dtos.ChatMessageResponseDTO;
import dev.jenny.clara.chat.dtos.ContactSummaryDTO;

public class ChatMapper {

    private ChatMapper() {
        throw new UnsupportedOperationException("No se puede instanciar esta clase de utilidad");
    }

    public static ChatMessageResponseDTO toMessageDTO(ChatMessageEntity message, boolean read) {
        return new ChatMessageResponseDTO(message.getId(), message.getContact(), message.getCharacter(),
                message.getContent(), message.getDisplayOrder(), read);
    }

    public static ContactSummaryDTO toContactSummaryDTO(String contact, String lastMessage, int unread) {
        return new ContactSummaryDTO(contact, lastMessage, unread);
    }
}
