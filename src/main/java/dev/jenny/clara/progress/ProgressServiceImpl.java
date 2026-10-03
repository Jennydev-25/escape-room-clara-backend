package dev.jenny.clara.progress;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import dev.jenny.clara.progress.dtos.AccumulateTimeRequestDTO;
import dev.jenny.clara.progress.dtos.ProgressResponseDTO;
import dev.jenny.clara.progress.dtos.UpdateNoteRequestDTO;
import dev.jenny.clara.progress.mappers.ProgressMapper;
import dev.jenny.clara.user.UserEntity;
import dev.jenny.clara.user.UserRepository;

@Service
public class ProgressServiceImpl implements InterfaceProgressService {

    private static final int DEFAULT_CHAPTER = 1;
    private static final String DEFAULT_HUD = "";
    private static final boolean DEFAULT_INVESTIGATION_SUBMITTED = false;
    private static final long DEFAULT_SECONDS = 0L;
    private static final String DEFAULT_NOTE = "";

    private final ProgressRepository repository;
    private final UserRepository userRepository;

    public ProgressServiceImpl(ProgressRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public ProgressResponseDTO getOrCreateProgress(Authentication authentication) {
        UserEntity user = userRepository.findByEmail(authentication.getName()).get();

        ProgressEntity progress = repository.findByUserId(user.getId())
                .orElseGet(() -> repository.save(createDefaultProgress(user)));

        return ProgressMapper.toDTO(progress);
    }

    private ProgressEntity createDefaultProgress(UserEntity user) {
        return ProgressEntity.builder()
                .user(user)
                .currentChapter(DEFAULT_CHAPTER)
                .hudLetters(DEFAULT_HUD)
                .investigationSubmitted(DEFAULT_INVESTIGATION_SUBMITTED)
                .timeSpentSeconds(DEFAULT_SECONDS)
                .freeNote(DEFAULT_NOTE)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public ProgressResponseDTO accumulateTime(Authentication authentication, AccumulateTimeRequestDTO dto) {
        throw new UnsupportedOperationException("Método no implementado todavía");
    }

    @Override
    public ProgressResponseDTO updateNote(Authentication authentication, UpdateNoteRequestDTO dto) {
        throw new UnsupportedOperationException("Método no implementado todavía");
    }
}