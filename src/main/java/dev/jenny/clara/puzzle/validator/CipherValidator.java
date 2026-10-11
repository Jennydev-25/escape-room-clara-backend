package dev.jenny.clara.puzzle.validator;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import dev.jenny.clara.expectedanswer.ExpectedAnswerEntity;
import dev.jenny.clara.puzzle.PuzzleEntity;
import dev.jenny.clara.puzzle.PuzzleType;
import dev.jenny.clara.puzzle.dtos.PuzzleAnswerRequestDTO;

@Component
public class CipherValidator implements PuzzleValidator {

    private final PasswordEncoder passwordEncoder;

    public CipherValidator(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(PuzzleType type) {
        return type == PuzzleType.CIFRADO;
    }

    @Override
    public boolean isCorrect(PuzzleEntity puzzle, PuzzleAnswerRequestDTO answer, List<ExpectedAnswerEntity> expected) {
        String storedHash = expected.get(0).getAnswerHash();
        String normalizedAnswer = AnswerNormalizer.normalize(answer.value());

        return passwordEncoder.matches(normalizedAnswer, storedHash);
    }
}
