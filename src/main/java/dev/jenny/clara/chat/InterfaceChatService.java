package dev.jenny.clara.chat;

import java.util.List;

import org.springframework.security.core.Authentication;

import dev.jenny.clara.chat.dtos.ChatMessageResponseDTO;
import dev.jenny.clara.chat.dtos.ContactSummaryDTO;

public interface InterfaceChatService {

    List<ContactSummaryDTO> listContacts(Authentication authentication);

    List<ChatMessageResponseDTO> listConversation(Authentication authentication, String contact);
}
