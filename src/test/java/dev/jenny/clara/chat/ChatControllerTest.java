package dev.jenny.clara.chat;

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

import dev.jenny.clara.chat.dtos.ChatMessageResponseDTO;
import dev.jenny.clara.chat.dtos.ContactSummaryDTO;
import dev.jenny.clara.chat.exceptions.ContactNotFoundException;
import dev.jenny.clara.config.SecurityConfig;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = ChatController.class)
@Import(SecurityConfig.class)
class ChatControllerTest {

    private static final String CONTACT = "Diego Barreda";
    private static final String CHARACTER = "Clara";
    private static final String CONTENT = "Buenas tardes, Sr. Barreda.";
    private static final Long MESSAGE_ID = 1L;
    private static final int DISPLAY_ORDER = 1;
    private static final String MISSING_CONTACT = "Nadie";
    private static final String NOT_FOUND_MESSAGE = "No se encontró ningún contacto con el nombre " + MISSING_CONTACT;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InterfaceChatService chatService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testListContacts_ShouldReturnVisibleContacts() throws Exception {
        List<ContactSummaryDTO> contacts = List.of(new ContactSummaryDTO(CONTACT, CONTENT, 1));
        String responseJson = mapper.writeValueAsString(contacts);

        when(chatService.listContacts(any(Authentication.class))).thenReturn(contacts);

        mockMvc.perform(get("/api/v1/chat"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testGetConversation_ShouldReturnMessages() throws Exception {
        List<ChatMessageResponseDTO> conversation = List
                .of(new ChatMessageResponseDTO(MESSAGE_ID, CONTACT, CHARACTER, CONTENT, DISPLAY_ORDER, true));
        String responseJson = mapper.writeValueAsString(conversation);

        when(chatService.listConversation(any(Authentication.class), eq(CONTACT))).thenReturn(conversation);

        mockMvc.perform(get("/api/v1/chat/{contact}", CONTACT))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testGetConversation_ShouldReturnNotFound_WhenContactDoesNotExist() throws Exception {
        when(chatService.listConversation(any(Authentication.class), eq(MISSING_CONTACT)))
                .thenThrow(new ContactNotFoundException(NOT_FOUND_MESSAGE));

        mockMvc.perform(get("/api/v1/chat/{contact}", MISSING_CONTACT))
                .andExpect(status().isNotFound());
    }
}
