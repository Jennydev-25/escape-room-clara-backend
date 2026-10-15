package dev.jenny.clara.seen;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.jenny.clara.config.SecurityConfig;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = SeenItemController.class)
@Import(SecurityConfig.class)
class SeenItemControllerTest {

    private static final String FOLDER_KEY = "folder:leeme";
    private static final String FILE_KEY = "file:/tomas/tomas_elorza.png";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InterfaceSeenItemService seenItemService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testGetSeenKeys_ShouldReturnTheKeysOfTheUser() throws Exception {
        List<String> keys = List.of(FOLDER_KEY, FILE_KEY);
        String responseJson = mapper.writeValueAsString(keys);

        when(seenItemService.getSeenKeys(any(Authentication.class))).thenReturn(keys);

        mockMvc.perform(get("/api/v1/seen"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }
}
