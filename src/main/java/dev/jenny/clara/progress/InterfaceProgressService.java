package dev.jenny.clara.progress;

import org.springframework.security.core.Authentication;

import dev.jenny.clara.progress.dtos.AccumulateTimeRequestDTO;
import dev.jenny.clara.progress.dtos.ProgressResponseDTO;
import dev.jenny.clara.progress.dtos.UpdateNoteRequestDTO;
import dev.jenny.clara.user.UserEntity;

public interface InterfaceProgressService {

    ProgressResponseDTO getOrCreateProgress(Authentication authentication);

    void createDefaultProgress(UserEntity user);

    ProgressResponseDTO accumulateTime(Authentication authentication, AccumulateTimeRequestDTO dto);

    ProgressResponseDTO updateNote(Authentication authentication, UpdateNoteRequestDTO dto);

    ProgressResponseDTO markWelcomeSeen(Authentication authentication);

    ProgressResponseDTO advanceChapter(Authentication authentication, int newChapter);

}
