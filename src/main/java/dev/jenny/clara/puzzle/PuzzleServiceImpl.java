package dev.jenny.clara.puzzle;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import dev.jenny.clara.expectedanswer.ExpectedAnswerEntity;
import dev.jenny.clara.expectedanswer.ExpectedAnswerRepository;
import dev.jenny.clara.folder.FolderEntity;
import dev.jenny.clara.folder.FolderRepository;
import dev.jenny.clara.progress.InterfaceProgressService;
import dev.jenny.clara.progress.dtos.ProgressResponseDTO;
import dev.jenny.clara.puzzle.dtos.PuzzleAnswerRequestDTO;
import dev.jenny.clara.puzzle.dtos.PuzzleHintResponseDTO;
import dev.jenny.clara.puzzle.dtos.PuzzleResponseDTO;
import dev.jenny.clara.puzzle.dtos.ValidatedAnswerResponseDTO;
import dev.jenny.clara.puzzle.exceptions.HintNotFoundException;
import dev.jenny.clara.puzzle.exceptions.PuzzleChapterNotReachedException;
import dev.jenny.clara.puzzle.exceptions.PuzzleNotFoundException;
import dev.jenny.clara.puzzle.mappers.PuzzleMapper;
import dev.jenny.clara.puzzle.validator.PuzzleValidator;
import dev.jenny.clara.puzzle.validator.PuzzleValidatorResolver;

@Service
public class PuzzleServiceImpl implements InterfacePuzzleService {

    private final PuzzleRepository puzzleRepository;
    private final ExpectedAnswerRepository expectedAnswerRepository;
    private final PuzzleValidatorResolver puzzleValidatorResolver;
    private final InterfaceProgressService progressService;
    private final FolderRepository folderRepository;

    public PuzzleServiceImpl(PuzzleRepository puzzleRepository, ExpectedAnswerRepository expectedAnswerRepository,
            PuzzleValidatorResolver puzzleValidatorResolver, InterfaceProgressService progressService,
            FolderRepository folderRepository) {
        this.puzzleRepository = puzzleRepository;
        this.expectedAnswerRepository = expectedAnswerRepository;
        this.puzzleValidatorResolver = puzzleValidatorResolver;
        this.progressService = progressService;
        this.folderRepository = folderRepository;
    }

    @Override
    public PuzzleResponseDTO getPuzzle(Long puzzleId) {
        PuzzleEntity puzzle = findPuzzle(puzzleId);

        return PuzzleMapper.toDTO(puzzle);
    }

    @Override
    public PuzzleHintResponseDTO getHint(Authentication authentication, Long puzzleId, int number) {
        PuzzleEntity puzzle = findPuzzle(puzzleId);

        ProgressResponseDTO currentProgress = progressService.getOrCreateProgress(authentication);

        checkChapterReached(currentProgress, puzzle, puzzleId);

        List<String> hints = Stream.of(puzzle.getHint1(), puzzle.getHint2(), puzzle.getHint3()).toList();

        if (number < 1 || number > hints.size() || hints.get(number - 1) == null) {
            throw new HintNotFoundException("La prueba " + puzzleId + " no tiene la pista " + number);
        }

        return new PuzzleHintResponseDTO(number, hints.get(number - 1));
    }

    @Override
    public ValidatedAnswerResponseDTO validateAnswer(Authentication authentication, Long puzzleId,
            PuzzleAnswerRequestDTO answer) {
        PuzzleEntity puzzle = findPuzzle(puzzleId);

        ProgressResponseDTO currentProgress = progressService.getOrCreateProgress(authentication);

        checkChapterReached(currentProgress, puzzle, puzzleId);

        List<ExpectedAnswerEntity> expectedAnswers = expectedAnswerRepository.findByPuzzleId(puzzleId);

        PuzzleValidator validator = puzzleValidatorResolver.resolve(puzzle.getType());
        boolean correct = validator.isCorrect(puzzle, answer, expectedAnswers);

        if (!correct) {
            return new ValidatedAnswerResponseDTO(false, false, null, List.of());
        }

        if (currentProgress.currentChapter() != puzzle.getMinChapter()) {
            return new ValidatedAnswerResponseDTO(true, false, null, List.of());
        }

        int newChapter = puzzle.getMinChapter() + 1;
        ProgressResponseDTO advancedProgress = progressService.advanceChapter(authentication, newChapter);

        List<String> unlocks = folderRepository.findByMinChapterToOpen(newChapter).stream()
                .map(FolderEntity::getCode)
                .toList();

        return new ValidatedAnswerResponseDTO(true, true, advancedProgress.lastHudLetterAdded(), unlocks);
    }

    private PuzzleEntity findPuzzle(Long puzzleId) {
        return puzzleRepository.findById(puzzleId)
                .orElseThrow(() -> new PuzzleNotFoundException("No se encontró ninguna prueba con el id " + puzzleId));
    }

    private void checkChapterReached(ProgressResponseDTO currentProgress, PuzzleEntity puzzle, Long puzzleId) {
        if (currentProgress.currentChapter() < puzzle.getMinChapter()) {
            throw new PuzzleChapterNotReachedException(
                    "La prueba " + puzzleId + " todavía no está disponible en el capítulo actual");
        }
    }
}
