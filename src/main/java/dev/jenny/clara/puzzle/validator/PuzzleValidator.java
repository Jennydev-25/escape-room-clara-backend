package dev.jenny.clara.puzzle.validator;

import java.util.List;

import dev.jenny.clara.expectedanswer.ExpectedAnswerEntity;
import dev.jenny.clara.puzzle.PuzzleEntity;
import dev.jenny.clara.puzzle.dtos.PlayerAnswerRequestDTO;

public interface PuzzleValidator {

    boolean isCorrect(PuzzleEntity puzzle, PlayerAnswerRequestDTO answer, List<ExpectedAnswerEntity> expected);
}
