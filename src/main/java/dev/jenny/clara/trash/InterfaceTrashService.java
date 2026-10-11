package dev.jenny.clara.trash;

import java.util.List;

import org.springframework.security.core.Authentication;

import dev.jenny.clara.trash.dtos.DiscardedFileDTO;

public interface InterfaceTrashService {

    List<DiscardedFileDTO> listDiscardedFiles(Authentication authentication);
}
