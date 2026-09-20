package dev.jenny.clara.register;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import dev.jenny.clara.config.SecurityConfig;
import dev.jenny.clara.register.dtos.RegisterRequestDTO;
import dev.jenny.clara.register.dtos.RegisterResponseDTO;
import dev.jenny.clara.register.exceptions.EmailAlreadyExistsException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = RegisterController.class)
@Import(SecurityConfig.class)
class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InterfaceRegisterService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testRegister_ShouldReturnCreated() throws Exception {
        RegisterRequestDTO requestDto = new RegisterRequestDTO("clara@pruebas.com", "plainPassword", "plainPassword",
                "valid-captcha-token");
        RegisterResponseDTO responseDto = new RegisterResponseDTO("Usuario registrado correctamente");
        String requestJson = mapper.writeValueAsString(requestDto);
        String responseJson = mapper.writeValueAsString(responseDto);

        when(service.register(requestDto)).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), is(equalTo(201)));
        assertThat(response.getContentAsString(), is(equalTo(responseJson)));
    }

    @Test
    void testRegister_ShouldReturnConflict_WhenEmailAlreadyExists() throws Exception {
        RegisterRequestDTO requestDto = new RegisterRequestDTO("clara@pruebas.com", "plainPassword", "plainPassword",
                "valid-captcha-token");
        String requestJson = mapper.writeValueAsString(requestDto);
        String errorMessage = "El email ya está registrado.";

        when(service.register(requestDto)).thenThrow(new EmailAlreadyExistsException(errorMessage));

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isConflict());
    }

    @ParameterizedTest
    @MethodSource("invalidRegisterRequests")
    void testRegister_ShouldReturnBadRequest_WhenRequestIsInvalid(RegisterRequestDTO invalidRequest) throws Exception {
        String requestJson = mapper.writeValueAsString(invalidRequest);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    private static Stream<RegisterRequestDTO> invalidRegisterRequests() {
        return Stream.of(
                new RegisterRequestDTO("", "plainPassword", "plainPassword", "valid-captcha-token"),
                new RegisterRequestDTO("not-an-email", "plainPassword", "plainPassword", "valid-captcha-token"),
                new RegisterRequestDTO("clara@pruebas.com", "", "", "valid-captcha-token"),
                new RegisterRequestDTO("clara@pruebas.com", "short1", "short1", "valid-captcha-token"),
                new RegisterRequestDTO("clara@pruebas.com", "plainPassword", "differentPassword",
                        "valid-captcha-token"),
                new RegisterRequestDTO("clara@pruebas.com", "plainPassword", "plainPassword", ""));
    }
}