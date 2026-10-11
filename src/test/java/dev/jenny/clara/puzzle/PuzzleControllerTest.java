package dev.jenny.clara.puzzle;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.jenny.clara.config.SecurityConfig;
import dev.jenny.clara.puzzle.dtos.PuzzleAnswerRequestDTO;
import dev.jenny.clara.puzzle.dtos.PuzzleResponseDTO;
import dev.jenny.clara.puzzle.dtos.ValidatedAnswerResponseDTO;
import dev.jenny.clara.puzzle.exceptions.PuzzleNotFoundException;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = PuzzleController.class)
@Import(SecurityConfig.class)
class PuzzleControllerTest {

    private static final Long EXISTING_PUZZLE_ID = 1L;
    private static final PuzzleType PUZZLE_TYPE = PuzzleType.SIMPLE_PASSWORD;
    private static final int PUZZLE_MIN_CHAPTER = 1;
    private static final int PUZZLE_DISPLAY_ORDER = 1;
    private static final String PUZZLE_CONTENT = "contenido de la prueba";
    private static final Long MISSING_PUZZLE_ID = 99L;
    private static final String NOT_FOUND_MESSAGE = "No se encontró ninguna prueba con el id " + MISSING_PUZZLE_ID;
    private static final String ANSWER_VALUE = "any-value";
    private static final String UNLOCKED_FOLDER_CODE = "02_FRAUDE";
    private static final String NEW_HUD_LETTER = "M";

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

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testGetPuzzle_ShouldReturnNotFound_WhenPuzzleDoesNotExist() throws Exception {
        when(puzzleService.getPuzzle(eq(MISSING_PUZZLE_ID))).thenThrow(new PuzzleNotFoundException(NOT_FOUND_MESSAGE));

        mockMvc.perform(get("/api/v1/puzzles/{id}", MISSING_PUZZLE_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testValidateAnswer_ShouldReturnValidatedAnswer() throws Exception {
        ValidatedAnswerResponseDTO responseDto = new ValidatedAnswerResponseDTO(true, true, NEW_HUD_LETTER,
                List.of(UNLOCKED_FOLDER_CODE));
        String responseJson = mapper.writeValueAsString(responseDto);

        when(puzzleService.validateAnswer(any(Authentication.class), eq(EXISTING_PUZZLE_ID),
                any(PuzzleAnswerRequestDTO.class))).thenReturn(responseDto);

        PuzzleAnswerRequestDTO requestDto = new PuzzleAnswerRequestDTO(ANSWER_VALUE);

        mockMvc.perform(post("/api/v1/puzzles/{id}/answer", EXISTING_PUZZLE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }
}
