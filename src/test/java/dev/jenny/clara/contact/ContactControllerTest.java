package dev.jenny.clara.contact;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.jenny.clara.config.SecurityConfig;
import dev.jenny.clara.contact.dtos.ContactRequestDTO;
import dev.jenny.clara.contact.dtos.ContactResponseDTO;
import dev.jenny.clara.recaptcha.exceptions.InvalidRecaptchaException;
import dev.jenny.clara.security.SecurityUser;
import dev.jenny.clara.user.Role;
import dev.jenny.clara.user.User;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = ContactController.class)
@Import(SecurityConfig.class)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InterfaceContactService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testSend_ShouldReturnCreated() throws Exception {
        ContactRequestDTO requestDto = new ContactRequestDTO("Jugador de prueba", "jugador@pruebas.com",
                ContactType.QUESTION, "No encuentro dónde seguir en la carpeta del incendio.", "valid-captcha-token");
        ContactResponseDTO responseDto = new ContactResponseDTO(
                "Mensaje recibido correctamente. Tendrás respuesta en menos de 24/48 horas.");
        String requestJson = mapper.writeValueAsString(requestDto);
        String responseJson = mapper.writeValueAsString(responseDto);

        when(service.send(requestDto, null)).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/contact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), is(equalTo(201)));
        assertThat(response.getContentAsString(), is(equalTo(responseJson)));
    }

    @Test
    void testSend_ShouldPassAuthenticatedUserToService_WhenUserIsLoggedIn() throws Exception {
        ContactRequestDTO requestDto = new ContactRequestDTO("Jugador de prueba", "jugador@pruebas.com",
                ContactType.QUESTION, "No encuentro dónde seguir en la carpeta del incendio.", "valid-captcha-token");
        ContactResponseDTO responseDto = new ContactResponseDTO(
                "Mensaje recibido correctamente. Tendrás respuesta en menos de 24/48 horas.");
        User loggedInUser = User.builder().id(1L).email("jugador@pruebas.com").role(Role.USER).build();
        SecurityUser securityUser = new SecurityUser(loggedInUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(securityUser, null,
                securityUser.getAuthorities());
        String requestJson = mapper.writeValueAsString(requestDto);

        when(service.send(requestDto, loggedInUser)).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/contact")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated());

        verify(service).send(requestDto, loggedInUser);
    }

    @Test
    void testSend_ShouldReturnBadRequest_WhenRecaptchaIsInvalid() throws Exception {
        ContactRequestDTO requestDto = new ContactRequestDTO("Jugador de prueba", "jugador@pruebas.com",
                ContactType.QUESTION, "No encuentro dónde seguir en la carpeta del incendio.",
                "invalid-captcha-token");
        String requestJson = mapper.writeValueAsString(requestDto);
        String errorMessage = "El captcha no es válido.";

        when(service.send(requestDto, null)).thenThrow(new InvalidRecaptchaException(errorMessage));

        mockMvc.perform(post("/api/v1/contact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("invalidContactRequests")
    void testSend_ShouldReturnBadRequest_WhenRequestIsInvalid(ContactRequestDTO invalidRequest) throws Exception {
        String requestJson = mapper.writeValueAsString(invalidRequest);

        mockMvc.perform(post("/api/v1/contact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    private static Stream<ContactRequestDTO> invalidContactRequests() {
        return Stream.of(
                new ContactRequestDTO("", "jugador@pruebas.com", ContactType.QUESTION,
                        "No encuentro dónde seguir en la carpeta del incendio.", "valid-captcha-token"),
                new ContactRequestDTO("Jugador de prueba", "not-an-email", ContactType.QUESTION,
                        "No encuentro dónde seguir en la carpeta del incendio.", "valid-captcha-token"),
                new ContactRequestDTO("Jugador de prueba", "jugador@pruebas.com", null,
                        "No encuentro dónde seguir en la carpeta del incendio.", "valid-captcha-token"),
                new ContactRequestDTO("Jugador de prueba", "jugador@pruebas.com", ContactType.QUESTION, "",
                        "valid-captcha-token"),
                new ContactRequestDTO("Jugador de prueba", "jugador@pruebas.com", ContactType.QUESTION,
                        "No encuentro dónde seguir en la carpeta del incendio.", ""));
    }
}