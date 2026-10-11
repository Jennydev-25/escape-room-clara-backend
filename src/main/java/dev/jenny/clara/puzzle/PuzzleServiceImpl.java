package dev.jenny.clara.puzzle;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import dev.jenny.clara.expectedanswer.ExpectedAnswerEntity;
import dev.jenny.clara.expectedanswer.ExpectedAnswerRepository;
import dev.jenny.clara.progress.InterfaceProgressService;
import dev.jenny.clara.progress.dtos.ProgressResponseDTO;
import dev.jenny.clara.puzzle.dtos.PuzzleAnswerRequestDTO;
import dev.jenny.clara.puzzle.dtos.ValidatedAnswerResponseDTO;
import dev.jenny.clara.puzzle.exceptions.PuzzleNotFoundException;
import dev.jenny.clara.puzzle.validator.PuzzleValidator;
import dev.jenny.clara.puzzle.validator.PuzzleValidatorResolver;

@Service
public class PuzzleServiceImpl implements InterfacePuzzleService {

    private final PuzzleRepository puzzleRepository;
    private final ExpectedAnswerRepository expectedAnswerRepository;
    private final PuzzleValidatorResolver puzzleValidatorResolver;
    private final InterfaceProgressService progressService;

    public PuzzleServiceImpl(PuzzleRepository puzzleRepository, ExpectedAnswerRepository expectedAnswerRepository,
            PuzzleValidatorResolver puzzleValidatorResolver, InterfaceProgressService progressService) {
        this.puzzleRepository = puzzleRepository;
        this.expectedAnswerRepository = expectedAnswerRepository;
        this.puzzleValidatorResolver = puzzleValidatorResolver;
        this.progressService = progressService;
    }

    @Override
    public ValidatedAnswerResponseDTO validateAnswer(Authentication authentication, Long puzzleId,
            PuzzleAnswerRequestDTO answer) {
        PuzzleEntity puzzle = puzzleRepository.findById(puzzleId)
                .orElseThrow(() -> new PuzzleNotFoundException("No se encontró ninguna prueba con el id " + puzzleId));

        List<ExpectedAnswerEntity> expectedAnswers = expectedAnswerRepository.findByPuzzleId(puzzleId);

        PuzzleValidator validator = puzzleValidatorResolver.resolve(puzzle.getType());
        boolean correct = validator.isCorrect(puzzle, answer, expectedAnswers);

        if (!correct) {
            return new ValidatedAnswerResponseDTO(false, false, null, List.of());
        }

        ProgressResponseDTO currentProgress = progressService.getOrCreateProgress(authentication);

        if (currentProgress.currentChapter() != puzzle.getMinChapter()) {
            return new ValidatedAnswerResponseDTO(true, false, null, List.of());
        }

        ProgressResponseDTO advancedProgress = progressService.advanceChapter(authentication,
                puzzle.getMinChapter() + 1);

        return new ValidatedAnswerResponseDTO(true, true, advancedProgress.lastHudLetterAdded(), List.of());
    }
}
