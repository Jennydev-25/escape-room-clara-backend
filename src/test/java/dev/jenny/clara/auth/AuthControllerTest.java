package dev.jenny.clara.auth;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.jenny.clara.auth.dtos.LoginResponseDTO;
import dev.jenny.clara.auth.dtos.LogoutRequestDTO;
import dev.jenny.clara.auth.dtos.LogoutResponseDTO;
import dev.jenny.clara.config.SecurityConfig;
import dev.jenny.clara.refreshtoken.dtos.RefreshTokenRequestDTO;
import dev.jenny.clara.refreshtoken.exceptions.InvalidRefreshTokenException;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InterfaceAuthService authService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testLogin_ShouldReturnAccessAndRefreshTokens() throws Exception {
        LoginResponseDTO responseDto = new LoginResponseDTO("fake.jwt.token", "fake.refresh.token");
        String responseJson = mapper.writeValueAsString(responseDto);

        when(authService.login(any(Authentication.class))).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/auth/login"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.getContentAsString(), is(equalTo(responseJson)));
    }

    @Test
    void testRefresh_ShouldReturnNewAccessAndRefreshTokens() throws Exception {
        RefreshTokenRequestDTO requestDto = new RefreshTokenRequestDTO("old-refresh-token");
        LoginResponseDTO responseDto = new LoginResponseDTO("new.jwt.token", "new-refresh-token");
        String requestJson = mapper.writeValueAsString(requestDto);
        String responseJson = mapper.writeValueAsString(responseDto);

        when(authService.refresh("old-refresh-token")).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.getContentAsString(), is(equalTo(responseJson)));
    }

    @Test
    void testRefresh_ShouldReturnUnauthorized_WhenRefreshTokenIsInvalid() throws Exception {
        RefreshTokenRequestDTO requestDto = new RefreshTokenRequestDTO("invalid-refresh-token");
        String requestJson = mapper.writeValueAsString(requestDto);
        String errorMessage = "El refresh token no existe";

        when(authService.refresh("invalid-refresh-token")).thenThrow(new InvalidRefreshTokenException(errorMessage));

        mockMvc.perform(post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "clara@pruebas.com")
    void testLogout_ShouldReturnConfirmationMessage() throws Exception {
        LogoutRequestDTO requestDto = new LogoutRequestDTO("fake-refresh-token");
        LogoutResponseDTO responseDto = new LogoutResponseDTO("Sesión cerrada correctamente");
        String requestJson = mapper.writeValueAsString(requestDto);
        String responseJson = mapper.writeValueAsString(responseDto);

        when(authService.logout(any(Authentication.class), eq("fake-refresh-token"))).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.getContentAsString(), is(equalTo(responseJson)));
    }
}
