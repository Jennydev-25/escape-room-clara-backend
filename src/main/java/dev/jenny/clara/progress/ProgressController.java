package dev.jenny.clara.progress;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jenny.clara.progress.dtos.ProgressResponseDTO;

@RestController
@RequestMapping(path = "${api-endpoint}/progress")
public class ProgressController {

    private final InterfaceProgressService progressService;

    public ProgressController(InterfaceProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping("")
    public ProgressResponseDTO getProgress(Authentication authentication) {
        return progressService.getOrCreateProgress(authentication);
    }
}