package dev.jenny.clara.puzzle;

import org.springframework.security.core.Authentication;

import dev.jenny.clara.puzzle.dtos.PuzzleAnswerRequestDTO;
import dev.jenny.clara.puzzle.dtos.ValidatedAnswerResponseDTO;

public interface InterfacePuzzleService {

    ValidatedAnswerResponseDTO validateAnswer(Authentication authentication, Long puzzleId,
            PuzzleAnswerRequestDTO answer);
}
