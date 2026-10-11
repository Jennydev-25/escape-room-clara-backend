package dev.jenny.clara.puzzle;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.jenny.clara.config.SecurityConfig;
import dev.jenny.clara.puzzle.dtos.PuzzleResponseDTO;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = PuzzleController.class)
@Import(SecurityConfig.class)
class PuzzleControllerTest {

    private static final Long EXISTING_PUZZLE_ID = 1L;
    private static final PuzzleType PUZZLE_TYPE = PuzzleType.SIMPLE_PASSWORD;
    private static final int PUZZLE_MIN_CHAPTER = 1;
    private static final int PUZZLE_DISPLAY_ORDER = 1;
    private static final String PUZZLE_CONTENT = "contenido de la prueba";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InterfacePuzzleService puzzleService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testGetPuzzle_ShouldReturnPuzzle() throws Exception {
        PuzzleResponseDTO responseDto = new PuzzleResponseDTO(EXISTING_PUZZLE_ID, PUZZLE_TYPE, PUZZLE_MIN_CHAPTER,
                PUZZLE_DISPLAY_ORDER, PUZZLE_CONTENT);
        String responseJson = mapper.writeValueAsString(responseDto);

        when(puzzleService.getPuzzle(eq(EXISTING_PUZZLE_ID))).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/puzzles/{id}", EXISTING_PUZZLE_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }
}
