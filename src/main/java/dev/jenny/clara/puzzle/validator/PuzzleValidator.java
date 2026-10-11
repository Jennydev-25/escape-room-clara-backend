package dev.jenny.clara.puzzle.validator;

import java.util.List;

import dev.jenny.clara.expectedanswer.ExpectedAnswerEntity;
import dev.jenny.clara.puzzle.PuzzleEntity;
import dev.jenny.clara.puzzle.PuzzleType;
import dev.jenny.clara.puzzle.dtos.PuzzleAnswerRequestDTO;

public interface PuzzleValidator {

    boolean supports(PuzzleType type);

    boolean isCorrect(PuzzleEntity puzzle, PuzzleAnswerRequestDTO answer, List<ExpectedAnswerEntity> expected);
}
