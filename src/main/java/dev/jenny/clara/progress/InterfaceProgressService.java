package dev.jenny.clara.progress;

import org.springframework.security.core.Authentication;

import dev.jenny.clara.progress.dtos.AccumulateTimeRequestDTO;
import dev.jenny.clara.progress.dtos.ProgressResponseDTO;
import dev.jenny.clara.progress.dtos.UpdateNoteRequestDTO;

public interface InterfaceProgressService {

    ProgressResponseDTO getOrCreateProgress(Authentication authentication);

    ProgressResponseDTO accumulateTime(Authentication authentication, AccumulateTimeRequestDTO dto);

    ProgressResponseDTO updateNote(Authentication authentication, UpdateNoteRequestDTO dto);

}
