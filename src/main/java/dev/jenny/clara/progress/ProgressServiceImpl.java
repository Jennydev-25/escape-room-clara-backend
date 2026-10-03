package dev.jenny.clara.progress;

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

    private final ProgressRepository repository;
    private final UserRepository userRepository;

    public ProgressServiceImpl(ProgressRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public ProgressResponseDTO getOrCreateProgress(Authentication authentication) {
        UserEntity user = userRepository.findByEmail(authentication.getName()).get();
        ProgressEntity progress = repository.findByUserId(user.getId()).get();

        return ProgressMapper.toDTO(progress);
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
