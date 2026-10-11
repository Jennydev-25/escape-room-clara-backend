package dev.jenny.clara.puzzle;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import dev.jenny.clara.puzzle.dtos.PuzzleAnswerRequestDTO;
import dev.jenny.clara.puzzle.dtos.ValidatedAnswerResponseDTO;
import dev.jenny.clara.puzzle.exceptions.PuzzleNotFoundException;

@Service
public class PuzzleServiceImpl implements InterfacePuzzleService {

    private final PuzzleRepository puzzleRepository;

    public PuzzleServiceImpl(PuzzleRepository puzzleRepository) {
        this.puzzleRepository = puzzleRepository;
    }

    @Override
    public ValidatedAnswerResponseDTO validateAnswer(Authentication authentication, Long puzzleId,
            PuzzleAnswerRequestDTO answer) {
        puzzleRepository.findById(puzzleId)
                .orElseThrow(() -> new PuzzleNotFoundException("No se encontró ninguna prueba con el id " + puzzleId));

        return null;
    }
}
