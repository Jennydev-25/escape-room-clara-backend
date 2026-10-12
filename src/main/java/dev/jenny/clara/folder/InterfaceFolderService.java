package dev.jenny.clara.folder;

import java.util.List;

import org.springframework.security.core.Authentication;

import dev.jenny.clara.folder.dtos.FolderResponseDTO;
import dev.jenny.clara.folder.dtos.FolderSummaryDTO;

public interface InterfaceFolderService {

    FolderResponseDTO getVisibleFolder(Authentication authentication, String code);

    List<FolderSummaryDTO> listVisibleFolders(Authentication authentication);
}
