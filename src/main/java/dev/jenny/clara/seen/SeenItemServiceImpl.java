package dev.jenny.clara.seen;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import dev.jenny.clara.seen.dtos.SeenItemResponseDTO;
import dev.jenny.clara.seen.mappers.SeenItemMapper;
import dev.jenny.clara.user.UserEntity;
import dev.jenny.clara.user.UserRepository;

@Service
public class SeenItemServiceImpl implements InterfaceSeenItemService {

    private final SeenItemRepository seenItemRepository;
    private final UserRepository userRepository;

    public SeenItemServiceImpl(SeenItemRepository seenItemRepository, UserRepository userRepository) {
        this.seenItemRepository = seenItemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<SeenItemResponseDTO> getSeenItems(Authentication authentication) {
        UserEntity user = userRepository.findByEmail(authentication.getName()).get();

        return seenItemRepository.findByUserId(user.getId()).stream()
                .map(SeenItemMapper::toDTO)
                .toList();
    }

    @Override
    public void markSeen(Authentication authentication, String key) {
        UserEntity user = userRepository.findByEmail(authentication.getName()).get();

        if (seenItemRepository.existsByUserIdAndItemKey(user.getId(), key)) {
            return;
        }

        seenItemRepository.save(SeenItemEntity.builder()
                .user(user)
                .itemKey(key)
                .seenAt(LocalDateTime.now())
                .build());
    }
}
