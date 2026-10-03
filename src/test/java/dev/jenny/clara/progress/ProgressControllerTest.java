package dev.jenny.clara.progress;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.jenny.clara.config.SecurityConfig;
import dev.jenny.clara.progress.dtos.AccumulateTimeRequestDTO;
import dev.jenny.clara.progress.dtos.ProgressResponseDTO;
import dev.jenny.clara.progress.dtos.UpdateNoteRequestDTO;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = ProgressController.class)
@Import(SecurityConfig.class)
class ProgressControllerTest {

    private static final int TEST_CHAPTER = 1;
    private static final String TEST_HUD = "";
    private static final boolean TEST_SUBMITTED = false;
    private static final long TEST_SECONDS = 0L;
    private static final String TEST_NOTE = "";
    private static final LocalDateTime TEST_UPDATED_AT = LocalDateTime.of(2026, 1, 1, 10, 0);

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InterfaceProgressService progressService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testGetProgress_ShouldReturnCurrentProgress() throws Exception {
        ProgressResponseDTO responseDto = new ProgressResponseDTO(
                TEST_CHAPTER, TEST_HUD, TEST_SUBMITTED, TEST_SECONDS, TEST_NOTE, TEST_UPDATED_AT);
        String responseJson = mapper.writeValueAsString(responseDto);

        when(progressService.getOrCreateProgress(any(Authentication.class))).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/progress"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.getContentAsString(), is(equalTo(responseJson)));
    }

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testAccumulateTime_ShouldReturnUpdatedProgress() throws Exception {
        AccumulateTimeRequestDTO requestDto = new AccumulateTimeRequestDTO(60L);
        ProgressResponseDTO responseDto = new ProgressResponseDTO(
                TEST_CHAPTER, TEST_HUD, TEST_SUBMITTED, TEST_SECONDS, TEST_NOTE, TEST_UPDATED_AT);
        String requestJson = mapper.writeValueAsString(requestDto);
        String responseJson = mapper.writeValueAsString(responseDto);

        when(progressService.accumulateTime(any(Authentication.class), eq(requestDto))).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(patch("/api/v1/progress/time")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.getContentAsString(), is(equalTo(responseJson)));
    }

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testUpdateNote_ShouldReturnUpdatedProgress() throws Exception {
        UpdateNoteRequestDTO requestDto = new UpdateNoteRequestDTO("mi nota actualizada");
        ProgressResponseDTO responseDto = new ProgressResponseDTO(
                TEST_CHAPTER, TEST_HUD, TEST_SUBMITTED, TEST_SECONDS, TEST_NOTE, TEST_UPDATED_AT);
        String requestJson = mapper.writeValueAsString(requestDto);
        String responseJson = mapper.writeValueAsString(responseDto);

        when(progressService.updateNote(any(Authentication.class), eq(requestDto))).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(patch("/api/v1/progress/note")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.getContentAsString(), is(equalTo(responseJson)));
    }

    @ParameterizedTest
    @MethodSource("invalidAccumulateTimeRequests")
    @WithMockUser(username = "clara@pruebas.com")
    void testAccumulateTime_ShouldReturnBadRequest_WhenRequestIsInvalid(AccumulateTimeRequestDTO invalidRequest)
            throws Exception {
        String requestJson = mapper.writeValueAsString(invalidRequest);

        mockMvc.perform(patch("/api/v1/progress/time")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    private static Stream<AccumulateTimeRequestDTO> invalidAccumulateTimeRequests() {
        return Stream.of(
                new AccumulateTimeRequestDTO(null),
                new AccumulateTimeRequestDTO(-10L),
                new AccumulateTimeRequestDTO(0L));
    }

    @ParameterizedTest
    @MethodSource("invalidUpdateNoteRequests")
    @WithMockUser(username = "clara@pruebas.com")
    void testUpdateNote_ShouldReturnBadRequest_WhenRequestIsInvalid(UpdateNoteRequestDTO invalidRequest)
            throws Exception {
        String requestJson = mapper.writeValueAsString(invalidRequest);

        mockMvc.perform(patch("/api/v1/progress/note")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    private static Stream<UpdateNoteRequestDTO> invalidUpdateNoteRequests() {
        return Stream.of(
                new UpdateNoteRequestDTO(null));
    }
}