package dev.jenny.clara.seen;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
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
import dev.jenny.clara.seen.dtos.SeenItemRequestDTO;
import dev.jenny.clara.seen.dtos.SeenItemResponseDTO;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = SeenItemController.class)
@Import(SecurityConfig.class)
class SeenItemControllerTest {

    private static final String FOLDER_KEY = "folder:leeme";
    private static final String FILE_KEY = "file:/tomas/tomas_elorza.png";
    private static final LocalDateTime SEEN_AT = LocalDateTime.of(2026, 10, 15, 18, 30);

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InterfaceSeenItemService seenItemService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testGetSeenItems_ShouldReturnTheSeenItemsOfTheUser() throws Exception {
        List<SeenItemResponseDTO> items = List.of(
                new SeenItemResponseDTO(FOLDER_KEY, SEEN_AT),
                new SeenItemResponseDTO(FILE_KEY, SEEN_AT));
        String responseJson = mapper.writeValueAsString(items);

        when(seenItemService.getSeenItems(any(Authentication.class))).thenReturn(items);

        mockMvc.perform(get("/api/v1/seen"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testMarkSeen_ShouldReturnNoContent_AndSaveTheKey() throws Exception {
        SeenItemRequestDTO requestDto = new SeenItemRequestDTO(FOLDER_KEY);
        String requestJson = mapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/v1/seen")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isNoContent());

        verify(seenItemService).markSeen(any(Authentication.class), eq(FOLDER_KEY));
    }

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testMarkSeen_ShouldReturnBadRequest_WhenKeyIsBlank() throws Exception {
        String requestJson = mapper.writeValueAsString(new SeenItemRequestDTO(" "));

        mockMvc.perform(post("/api/v1/seen")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());

        verify(seenItemService, never()).markSeen(any(Authentication.class), any(String.class));
    }
}
