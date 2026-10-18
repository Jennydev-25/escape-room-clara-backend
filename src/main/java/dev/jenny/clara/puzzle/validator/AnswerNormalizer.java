package dev.jenny.clara.puzzle.validator;

final class AnswerNormalizer {

    private AnswerNormalizer() {
        throw new UnsupportedOperationException();
    }

    static String normalize(String rawAnswer) {
        return rawAnswer.trim();
    }
}
