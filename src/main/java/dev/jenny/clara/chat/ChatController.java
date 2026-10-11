package dev.jenny.clara.chat;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jenny.clara.chat.dtos.ChatMessageResponseDTO;
import dev.jenny.clara.chat.dtos.ContactSummaryDTO;

@RestController
@RequestMapping(path = "${api-endpoint}/chat")
public class ChatController {

    private final InterfaceChatService chatService;

    public ChatController(InterfaceChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public List<ContactSummaryDTO> listContacts(Authentication authentication) {
        return chatService.listContacts(authentication);
    }

    @GetMapping("/{contact}")
    public List<ChatMessageResponseDTO> getConversation(Authentication authentication, @PathVariable String contact) {
        return chatService.listConversation(authentication, contact);
    }
}
