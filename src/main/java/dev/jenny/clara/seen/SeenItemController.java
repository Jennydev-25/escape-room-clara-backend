package dev.jenny.clara.seen;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jenny.clara.seen.dtos.SeenItemResponseDTO;

@RestController
@RequestMapping(path = "${api-endpoint}/seen")
public class SeenItemController {

    private final InterfaceSeenItemService seenItemService;

    public SeenItemController(InterfaceSeenItemService seenItemService) {
        this.seenItemService = seenItemService;
    }

    @GetMapping
    public List<SeenItemResponseDTO> getSeenItems(Authentication authentication) {
        return seenItemService.getSeenItems(authentication);
    }
}
