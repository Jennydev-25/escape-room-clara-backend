package dev.jenny.clara.trash;

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
import dev.jenny.clara.trash.dtos.DiscardedFileDTO;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = TrashController.class)
@Import(SecurityConfig.class)
class TrashControllerTest {

    private static final Long FILE_ID = 1L;
    private static final String NAME = "borrador_reportaje_puerto_v1.txt";
    private static final String TYPE = "TXT";
    private static final String BODY = "[BORRADOR — sin terminar]";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InterfaceTrashService trashService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testListDiscardedFiles_ShouldReturnVisibleFiles() throws Exception {
        List<DiscardedFileDTO> files = List.of(new DiscardedFileDTO(FILE_ID, NAME, TYPE, BODY));
        String responseJson = mapper.writeValueAsString(files);

        when(trashService.listDiscardedFiles(any(Authentication.class))).thenReturn(files);

        mockMvc.perform(get("/api/v1/trash"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }
}
