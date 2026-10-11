package dev.jenny.clara.chat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import dev.jenny.clara.chat.dtos.ChatMessageResponseDTO;
import dev.jenny.clara.chat.dtos.ContactSummaryDTO;
import dev.jenny.clara.chat.exceptions.ContactNotFoundException;
import dev.jenny.clara.chat.mappers.ChatMapper;
import dev.jenny.clara.progress.InterfaceProgressService;
import dev.jenny.clara.progress.dtos.ProgressResponseDTO;
import dev.jenny.clara.user.UserEntity;
import dev.jenny.clara.user.UserRepository;

@Service
public class ChatServiceImpl implements InterfaceChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageReadRepository chatMessageReadRepository;
    private final UserRepository userRepository;
    private final InterfaceProgressService progressService;

    public ChatServiceImpl(ChatMessageRepository chatMessageRepository,
            ChatMessageReadRepository chatMessageReadRepository, UserRepository userRepository,
            InterfaceProgressService progressService) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatMessageReadRepository = chatMessageReadRepository;
        this.userRepository = userRepository;
        this.progressService = progressService;
    }

    @Override
    public List<ContactSummaryDTO> listContacts(Authentication authentication) {
        UserEntity user = userRepository.findByEmail(authentication.getName()).get();
        ProgressResponseDTO progress = progressService.getOrCreateProgress(authentication);

        Set<Long> readMessageIds = chatMessageReadRepository.findByUserId(user.getId()).stream()
                .map(read -> read.getMessage().getId())
                .collect(Collectors.toSet());

        List<String> visibleContacts = chatMessageRepository
                .findDistinctContactByMinChapterLessThanEqual(progress.currentChapter());

        return visibleContacts.stream()
                .map(contact -> {
                    List<ChatMessageEntity> messages = chatMessageRepository
                            .findByContactAndMinChapterLessThanEqualOrderByDisplayOrderAsc(contact,
                                    progress.currentChapter());
                    String lastMessage = messages.get(messages.size() - 1).getContent();
                    int unread = (int) messages.stream()
                            .filter(message -> !readMessageIds.contains(message.getId()))
                            .count();
                    return ChatMapper.toContactSummaryDTO(contact, lastMessage, unread);
                })
                .toList();
    }

    @Override
    public List<ChatMessageResponseDTO> listConversation(Authentication authentication, String contact) {
        if (!chatMessageRepository.existsByContact(contact)) {
            throw new ContactNotFoundException("No se encontró ningún contacto con el nombre " + contact);
        }

        UserEntity user = userRepository.findByEmail(authentication.getName()).get();
        ProgressResponseDTO progress = progressService.getOrCreateProgress(authentication);

        List<ChatMessageEntity> messages = chatMessageRepository
                .findByContactAndMinChapterLessThanEqualOrderByDisplayOrderAsc(contact, progress.currentChapter());

        Set<Long> readMessageIds = chatMessageReadRepository.findByUserId(user.getId()).stream()
                .map(read -> read.getMessage().getId())
                .collect(Collectors.toSet());

        messages.stream()
                .filter(message -> !readMessageIds.contains(message.getId()))
                .forEach(message -> chatMessageReadRepository.save(ChatMessageReadEntity.builder()
                        .user(user)
                        .message(message)
                        .readAt(LocalDateTime.now())
                        .build()));

        return messages.stream()
                .map(message -> ChatMapper.toMessageDTO(message, true))
                .toList();
    }
}
