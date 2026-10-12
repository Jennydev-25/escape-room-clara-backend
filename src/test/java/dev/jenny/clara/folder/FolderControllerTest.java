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
import dev.jenny.clara.folder.dtos.FolderSummaryDTO;
import dev.jenny.clara.folder.exceptions.ChapterNotReachedException;
import dev.jenny.clara.folder.exceptions.FolderNotFoundException;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = FolderController.class)
@Import(SecurityConfig.class)
class FolderControllerTest {

    private static final String EXISTING_CODE = "01_TOMAS";
    private static final String FOLDER_NAME = "01_TOMAS";
    private static final String MISSING_CODE = "99_NO_EXISTE";
    private static final String NOT_FOUND_MESSAGE = "No se encontró ninguna carpeta con el código " + MISSING_CODE;
    private static final String LOCKED_CODE = "03_EMPRESAS";
    private static final String CHAPTER_NOT_REACHED_MESSAGE = "La carpeta " + LOCKED_CODE
            + " todavía no está disponible en el capítulo actual";

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

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testGetFolder_ShouldReturnNotFound_WhenFolderDoesNotExist() throws Exception {
        when(folderService.getVisibleFolder(any(Authentication.class), eq(MISSING_CODE)))
                .thenThrow(new FolderNotFoundException(NOT_FOUND_MESSAGE));

        mockMvc.perform(get("/api/v1/folders/{code}", MISSING_CODE))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testGetFolder_ShouldReturnForbidden_WhenChapterNotReached() throws Exception {
        when(folderService.getVisibleFolder(any(Authentication.class), eq(LOCKED_CODE)))
                .thenThrow(new ChapterNotReachedException(CHAPTER_NOT_REACHED_MESSAGE));

        mockMvc.perform(get("/api/v1/folders/{code}", LOCKED_CODE))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testListFolders_ShouldReturnVisibleFolders() throws Exception {
        List<FolderSummaryDTO> folders = List.of(new FolderSummaryDTO(EXISTING_CODE, FOLDER_NAME));
        String responseJson = mapper.writeValueAsString(folders);

        when(folderService.listVisibleFolders(any(Authentication.class))).thenReturn(folders);

        mockMvc.perform(get("/api/v1/folders"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }
}
