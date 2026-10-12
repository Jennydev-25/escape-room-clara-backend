package dev.jenny.clara.puzzle;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jenny.clara.puzzle.dtos.PuzzleAnswerRequestDTO;
import dev.jenny.clara.puzzle.dtos.PuzzleHintResponseDTO;
import dev.jenny.clara.puzzle.dtos.PuzzleResponseDTO;
import dev.jenny.clara.puzzle.dtos.ValidatedAnswerResponseDTO;

@RestController
@RequestMapping(path = "${api-endpoint}/puzzles/{id}")
public class PuzzleController {

    private final InterfacePuzzleService puzzleService;

    public PuzzleController(InterfacePuzzleService puzzleService) {
        this.puzzleService = puzzleService;
    }

    @GetMapping
    public PuzzleResponseDTO getPuzzle(@PathVariable Long id) {
        return puzzleService.getPuzzle(id);
    }

    @GetMapping("/hints/{number}")
    public PuzzleHintResponseDTO getHint(Authentication authentication, @PathVariable Long id,
            @PathVariable int number) {
        return puzzleService.getHint(authentication, id, number);
    }

    @PostMapping("/answer")
    public ValidatedAnswerResponseDTO validateAnswer(Authentication authentication, @PathVariable Long id,
            @RequestBody PuzzleAnswerRequestDTO answer) {
        return puzzleService.validateAnswer(authentication, id, answer);
    }
}
