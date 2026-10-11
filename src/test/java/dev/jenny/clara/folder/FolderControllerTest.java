package dev.jenny.clara.folder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import dev.jenny.clara.folder.dtos.FolderResponseDTO;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = FolderController.class)
@Import(SecurityConfig.class)
class FolderControllerTest {

    private static final String EXISTING_CODE = "01_TOMAS";
    private static final String FOLDER_NAME = "01_TOMAS";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InterfaceFolderService folderService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testGetFolder_ShouldReturnVisibleFolder() throws Exception {
        FolderResponseDTO responseDto = new FolderResponseDTO(EXISTING_CODE, FOLDER_NAME, List.of());
        String responseJson = mapper.writeValueAsString(responseDto);

        when(folderService.getVisibleFolder(any(Authentication.class), eq(EXISTING_CODE))).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/folders/{code}", EXISTING_CODE))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }
}
