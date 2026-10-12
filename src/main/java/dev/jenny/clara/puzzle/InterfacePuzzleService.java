package dev.jenny.clara.puzzle;

import org.springframework.security.core.Authentication;

import dev.jenny.clara.puzzle.dtos.PuzzleAnswerRequestDTO;
import dev.jenny.clara.puzzle.dtos.PuzzleHintResponseDTO;
import dev.jenny.clara.puzzle.dtos.PuzzleResponseDTO;
import dev.jenny.clara.puzzle.dtos.ValidatedAnswerResponseDTO;

public interface InterfacePuzzleService {

    PuzzleResponseDTO getPuzzle(Authentication authentication, Long puzzleId);

    PuzzleHintResponseDTO getHint(Authentication authentication, Long puzzleId, int number);

    ValidatedAnswerResponseDTO validateAnswer(Authentication authentication, Long puzzleId,
            PuzzleAnswerRequestDTO answer);
}
