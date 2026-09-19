package dev.jenny.clara.register;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.jenny.clara.register.dtos.RegisterRequestDTO;
import dev.jenny.clara.register.dtos.RegisterResponseDTO;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = RegisterController.class)
class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InterfaceRegisterService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testRegister_ShouldReturnCreated() throws Exception {
        RegisterRequestDTO requestDto = new RegisterRequestDTO("clara@pruebas.com", "plainPassword", "plainPassword");
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
}