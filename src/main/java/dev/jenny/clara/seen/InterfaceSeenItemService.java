package dev.jenny.clara.seen;

import java.util.List;

import org.springframework.security.core.Authentication;

public interface InterfaceSeenItemService {

    List<String> getSeenKeys(Authentication authentication);

    void markSeen(Authentication authentication, String key);
}
