package dev.jenny.clara.puzzle.validator;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.jenny.clara.puzzle.PuzzleType;
import dev.jenny.clara.puzzle.exceptions.UnsupportedPuzzleTypeException;

@Component
public class PuzzleValidatorResolver {

    private final List<PuzzleValidator> validators;

    public PuzzleValidatorResolver(List<PuzzleValidator> validators) {
        this.validators = validators;
    }

    public PuzzleValidator resolve(PuzzleType type) {
        return validators.stream()
                .filter(validator -> validator.supports(type))
                .findFirst()
                .orElseThrow(() -> new UnsupportedPuzzleTypeException(type));
    }
}
