package dev.jenny.clara.auth;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.jenny.clara.auth.dtos.LoginResponseDTO;
import dev.jenny.clara.config.SecurityConfig;
import dev.jenny.clara.globals.JwtService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testLogin_ShouldReturnToken() throws Exception {
        String token = "fake.jwt.token";
        LoginResponseDTO responseDto = new LoginResponseDTO(token);
        String responseJson = mapper.writeValueAsString(responseDto);

        when(jwtService.generateToken(any())).thenReturn(token);

        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/auth/login"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.getContentAsString(), is(equalTo(responseJson)));
    }
}