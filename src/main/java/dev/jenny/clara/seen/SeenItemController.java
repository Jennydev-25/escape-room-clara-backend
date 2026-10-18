package dev.jenny.clara.seen;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.jenny.clara.seen.dtos.SeenItemRequestDTO;
import dev.jenny.clara.seen.dtos.SeenItemResponseDTO;
import jakarta.validation.Valid;

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

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markSeen(Authentication authentication, @Valid @RequestBody SeenItemRequestDTO dto) {
        seenItemService.markSeen(authentication, dto.key());
    }
}
