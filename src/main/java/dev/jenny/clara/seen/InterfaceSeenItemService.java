package dev.jenny.clara.seen;

import java.util.List;

import org.springframework.security.core.Authentication;

import dev.jenny.clara.seen.dtos.SeenItemResponseDTO;

public interface InterfaceSeenItemService {

    List<SeenItemResponseDTO> getSeenItems(Authentication authentication);

    void markSeen(Authentication authentication, String key);
}
