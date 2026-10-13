package dev.jenny.clara.progress;

import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class HudService {

    private static final Map<Integer, String> LETTERS_BY_CHAPTER = Map.of(
            3, "M",
            4, "A",
            6, "R",
            7, "L",
            8, "E",
            11, "C",
            13, "H",
            17, "A");

    public String letterForChapter(int chapter) {
        return LETTERS_BY_CHAPTER.get(chapter);
    }
}
