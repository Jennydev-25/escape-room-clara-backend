package dev.jenny.clara.folder;

import org.springframework.security.core.Authentication;

import dev.jenny.clara.folder.dtos.FolderResponseDTO;

public interface InterfaceFolderService {

    FolderResponseDTO getVisibleFolder(Authentication authentication, String code);
}
