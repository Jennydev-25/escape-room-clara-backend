package dev.jenny.clara.puzzle.exceptions;

import dev.jenny.clara.puzzle.PuzzleType;

public class UnsupportedPuzzleTypeException extends RuntimeException {

    public UnsupportedPuzzleTypeException(PuzzleType type) {
        super("No hay ningún validador disponible para el tipo de prueba: " + type);
    }
}
