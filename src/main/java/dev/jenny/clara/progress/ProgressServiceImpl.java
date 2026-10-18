package dev.jenny.clara.progress;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final HudService hudService;

    public ProgressServiceImpl(ProgressRepository repository, UserRepository userRepository, HudService hudService) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.hudService = hudService;
    }

    @Override
    public ProgressResponseDTO getOrCreateProgress(Authentication authentication) {
        UserEntity user = userRepository.findByEmail(authentication.getName()).get();

        return ProgressMapper.toDTO(findOrCreateProgress(user));
    }

    private ProgressEntity findOrCreateProgress(UserEntity user) {
        return repository.findByUserId(user.getId())
                .orElseGet(() -> repository.save(buildDefaultProgress(user)));
    }

    @Override
    public void createDefaultProgress(UserEntity user) {
        repository.save(buildDefaultProgress(user));
    }

    private ProgressEntity buildDefaultProgress(UserEntity user) {
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
        UserEntity user = userRepository.findByEmail(authentication.getName()).get();
        ProgressEntity progress = findOrCreateProgress(user);

        progress.setTimeSpentSeconds(progress.getTimeSpentSeconds() + dto.secondsToAdd());
        progress.setUpdatedAt(LocalDateTime.now());

        return ProgressMapper.toDTO(repository.save(progress));
    }

    @Override
    public ProgressResponseDTO updateNote(Authentication authentication, UpdateNoteRequestDTO dto) {
        UserEntity user = userRepository.findByEmail(authentication.getName()).get();
        ProgressEntity progress = findOrCreateProgress(user);

        progress.setFreeNote(dto.freeNote());
        progress.setUpdatedAt(LocalDateTime.now());

        return ProgressMapper.toDTO(repository.save(progress));
    }

    @Override
    public ProgressResponseDTO markWelcomeSeen(Authentication authentication) {
        UserEntity user = userRepository.findByEmail(authentication.getName()).get();
        ProgressEntity progress = findOrCreateProgress(user);

        progress.setWelcomeSeen(true);
        progress.setUpdatedAt(LocalDateTime.now());

        return ProgressMapper.toDTO(repository.save(progress));
    }

    @Override
    @Transactional
    public ProgressResponseDTO advanceChapter(Authentication authentication, int newChapter) {
        UserEntity user = userRepository.findByEmail(authentication.getName()).get();
        ProgressEntity progress = findOrCreateProgress(user);

        progress.setCurrentChapter(newChapter);

        String newLetter = hudService.letterForChapter(newChapter);
        if (newLetter != null) {
            progress.setHudLetters(progress.getHudLetters() + newLetter);
        }

        progress.setUpdatedAt(LocalDateTime.now());

        return ProgressMapper.toDTO(repository.save(progress), newLetter);
    }
}
