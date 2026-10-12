package dev.jenny.clara.email;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
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
import dev.jenny.clara.email.dtos.EmailDetailDTO;
import dev.jenny.clara.email.dtos.EmailSummaryDTO;
import dev.jenny.clara.email.exceptions.EmailChapterNotReachedException;
import dev.jenny.clara.email.exceptions.EmailNotFoundException;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = EmailController.class)
@Import(SecurityConfig.class)
class EmailControllerTest {

    private static final Long EMAIL_ID = 1L;
    private static final String SENDER = "Marina Ortiz";
    private static final String RECIPIENT = "Clara Vega";
    private static final LocalDate SENT_DATE = LocalDate.of(2025, 6, 3);
    private static final String SUBJECT = "Lo de esta noche";
    private static final String BODY = "Nos vemos luego en el puerto.";
    private static final Long MISSING_EMAIL_ID = 99L;
    private static final String NOT_FOUND_MESSAGE = "No se encontró ningún correo con el id " + MISSING_EMAIL_ID;
    private static final Long LOCKED_EMAIL_ID = 2L;
    private static final String CHAPTER_NOT_REACHED_MESSAGE = "El correo " + LOCKED_EMAIL_ID
            + " todavía no está disponible en el capítulo actual";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InterfaceEmailService emailService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testListInbox_ShouldReturnVisibleEmails() throws Exception {
        List<EmailSummaryDTO> inbox = List.of(new EmailSummaryDTO(EMAIL_ID, SENDER, SUBJECT, false));
        String responseJson = mapper.writeValueAsString(inbox);

        when(emailService.listInbox(any(Authentication.class))).thenReturn(inbox);

        mockMvc.perform(get("/api/v1/emails"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testGetEmail_ShouldReturnEmailDetail() throws Exception {
        EmailDetailDTO detail = new EmailDetailDTO(EMAIL_ID, SENDER, RECIPIENT, SUBJECT, SENT_DATE, BODY);
        String responseJson = mapper.writeValueAsString(detail);

        when(emailService.getDetail(any(Authentication.class), eq(EMAIL_ID))).thenReturn(detail);

        mockMvc.perform(get("/api/v1/emails/{id}", EMAIL_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testGetEmail_ShouldReturnNotFound_WhenEmailDoesNotExist() throws Exception {
        when(emailService.getDetail(any(Authentication.class), eq(MISSING_EMAIL_ID)))
                .thenThrow(new EmailNotFoundException(NOT_FOUND_MESSAGE));

        mockMvc.perform(get("/api/v1/emails/{id}", MISSING_EMAIL_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testGetEmail_ShouldReturnForbidden_WhenChapterNotReached() throws Exception {
        when(emailService.getDetail(any(Authentication.class), eq(LOCKED_EMAIL_ID)))
                .thenThrow(new EmailChapterNotReachedException(CHAPTER_NOT_REACHED_MESSAGE));

        mockMvc.perform(get("/api/v1/emails/{id}", LOCKED_EMAIL_ID))
                .andExpect(status().isForbidden());
    }
}
