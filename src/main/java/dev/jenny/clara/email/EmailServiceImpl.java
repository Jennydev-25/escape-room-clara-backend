package dev.jenny.clara.email;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import dev.jenny.clara.email.dtos.EmailDetailDTO;
import dev.jenny.clara.email.dtos.EmailSummaryDTO;
import dev.jenny.clara.email.exceptions.EmailChapterNotReachedException;
import dev.jenny.clara.email.exceptions.EmailNotFoundException;
import dev.jenny.clara.email.mappers.EmailMapper;
import dev.jenny.clara.progress.InterfaceProgressService;
import dev.jenny.clara.progress.dtos.ProgressResponseDTO;
import dev.jenny.clara.user.UserEntity;
import dev.jenny.clara.user.UserRepository;

@Service
public class EmailServiceImpl implements InterfaceEmailService {

    private final EmailRepository emailRepository;
    private final EmailReadRepository emailReadRepository;
    private final UserRepository userRepository;
    private final InterfaceProgressService progressService;

    public EmailServiceImpl(EmailRepository emailRepository, EmailReadRepository emailReadRepository,
            UserRepository userRepository, InterfaceProgressService progressService) {
        this.emailRepository = emailRepository;
        this.emailReadRepository = emailReadRepository;
        this.userRepository = userRepository;
        this.progressService = progressService;
    }

    @Override
    public List<EmailSummaryDTO> listInbox(Authentication authentication) {
        UserEntity user = userRepository.findByEmail(authentication.getName()).get();
        ProgressResponseDTO progress = progressService.getOrCreateProgress(authentication);

        List<EmailEntity> visibleEmails = emailRepository
                .findByMinChapterLessThanEqualOrderByMinChapterAsc(progress.currentChapter());

        Set<Long> readEmailIds = emailReadRepository.findByUserId(user.getId()).stream()
                .map(read -> read.getEmail().getId())
                .collect(Collectors.toSet());

        return visibleEmails.stream()
                .map(email -> EmailMapper.toSummaryDTO(email, readEmailIds.contains(email.getId())))
                .toList();
    }

    @Override
    public EmailDetailDTO getDetail(Authentication authentication, Long emailId) {
        EmailEntity email = emailRepository.findById(emailId)
                .orElseThrow(() -> new EmailNotFoundException("No se encontró ningún correo con el id " + emailId));

        UserEntity user = userRepository.findByEmail(authentication.getName()).get();
        ProgressResponseDTO progress = progressService.getOrCreateProgress(authentication);

        if (progress.currentChapter() < email.getMinChapter()) {
            throw new EmailChapterNotReachedException(
                    "El correo " + emailId + " todavía no está disponible en el capítulo actual");
        }

        if (emailReadRepository.findByUserIdAndEmailId(user.getId(), emailId).isEmpty()) {
            emailReadRepository.save(EmailReadEntity.builder()
                    .user(user)
                    .email(email)
                    .readAt(LocalDateTime.now())
                    .build());
        }

        return EmailMapper.toDetailDTO(email);
    }
}
