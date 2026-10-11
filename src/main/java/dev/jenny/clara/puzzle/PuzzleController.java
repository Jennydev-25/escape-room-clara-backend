package dev.jenny.clara.puzzle;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jenny.clara.puzzle.dtos.PuzzleResponseDTO;

@RestController
@RequestMapping(path = "${api-endpoint}/puzzles")
public class PuzzleController {

    private final InterfacePuzzleService puzzleService;

    public PuzzleController(InterfacePuzzleService puzzleService) {
        this.puzzleService = puzzleService;
    }

    @GetMapping("/{id}")
    public PuzzleResponseDTO getPuzzle(@PathVariable Long id) {
        return puzzleService.getPuzzle(id);
    }
}
