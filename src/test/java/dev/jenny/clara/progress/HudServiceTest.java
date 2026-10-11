package dev.jenny.clara.progress;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class HudServiceTest {

    private final HudService hudService = new HudService();

    @ParameterizedTest
    @CsvSource({
            "3, M",
            "4, A",
            "6, R",
            "7, L",
            "8, E",
            "11, C",
            "13, H",
            "17, A"
    })
    void testLetterForChapter_ShouldReturnCorrespondingLetter_WhenChapterIsInflectionPoint(
            int chapter, String expectedLetter) {
        String letter = hudService.letterForChapter(chapter);

        assertThat(letter, is(equalTo(expectedLetter)));
    }
}
