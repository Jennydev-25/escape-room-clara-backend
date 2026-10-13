package dev.jenny.clara.folder;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jenny.clara.folder.dtos.FolderResponseDTO;
import dev.jenny.clara.folder.dtos.FolderSummaryDTO;

@RestController
@RequestMapping(path = "${api-endpoint}/folders")
public class FolderController {

    private final InterfaceFolderService folderService;

    public FolderController(InterfaceFolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping
    public List<FolderSummaryDTO> listFolders(Authentication authentication) {
        return folderService.listVisibleFolders(authentication);
    }

    @GetMapping("/{code}")
    public FolderResponseDTO getFolder(Authentication authentication, @PathVariable String code) {
        return folderService.getVisibleFolder(authentication, code);
    }
}
