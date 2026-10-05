package dev.jenny.clara.user;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.jenny.clara.config.SecurityConfig;
import dev.jenny.clara.register.exceptions.EmailAlreadyExistsException;
import dev.jenny.clara.user.dtos.ChangePasswordRequestDTO;
import dev.jenny.clara.user.dtos.UpdateProfileRequestDTO;
import dev.jenny.clara.user.dtos.UserProfileResponseDTO;
import dev.jenny.clara.user.exceptions.UserNotFoundException;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    private static final String TEST_ALIAS = "marta_v";
    private static final String TEST_EMAIL = "marta@pruebas.com";
    private static final Integer TEST_AVATAR_ID = 3;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InterfaceUserService userService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser(username = "marta@pruebas.com")
    void testGetProfile_ShouldReturnCurrentUserProfile() throws Exception {
        UserProfileResponseDTO responseDto = new UserProfileResponseDTO(TEST_ALIAS, TEST_EMAIL, TEST_AVATAR_ID);
        String responseJson = mapper.writeValueAsString(responseDto);

        when(userService.getProfile(any(Authentication.class))).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/users/me"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.getContentAsString(), is(equalTo(responseJson)));
    }

    @Test
    @WithMockUser(username = "marta@pruebas.com")
    void testGetProfile_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        when(userService.getProfile(any(Authentication.class)))
                .thenThrow(new UserNotFoundException("No se encontró ningún usuario con ese email"));

        mockMvc.perform(get("/api/v1/users/me"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "marta@pruebas.com")
    void testUpdateProfile_ShouldReturnUpdatedProfile() throws Exception {
        UpdateProfileRequestDTO requestDto = new UpdateProfileRequestDTO("nuevo_alias", TEST_EMAIL, TEST_EMAIL,
                TEST_AVATAR_ID);
        UserProfileResponseDTO responseDto = new UserProfileResponseDTO("nuevo_alias", TEST_EMAIL, TEST_AVATAR_ID);
        String requestJson = mapper.writeValueAsString(requestDto);
        String responseJson = mapper.writeValueAsString(responseDto);

        when(userService.updateProfile(any(Authentication.class), eq(requestDto))).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(put("/api/v1/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.getContentAsString(), is(equalTo(responseJson)));
    }

    @Test
    @WithMockUser(username = "marta@pruebas.com")
    void testChangePassword_ShouldReturnNoContent() throws Exception {
        ChangePasswordRequestDTO requestDto = new ChangePasswordRequestDTO("oldPass123", "newPass123", "newPass123");
        String requestJson = mapper.writeValueAsString(requestDto);

        mockMvc.perform(put("/api/v1/users/me/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "marta@pruebas.com")
    @ParameterizedTest(name = "{2} -> {1}")
    @MethodSource("updateProfileErrorCases")
    void testUpdateProfile_ShouldReturnErrorStatus_WhenServiceThrowsException(RuntimeException exception,
            int expectedStatus, String description) throws Exception {
        UpdateProfileRequestDTO requestDto = new UpdateProfileRequestDTO("nuevo_alias", TEST_EMAIL, TEST_EMAIL,
                TEST_AVATAR_ID);
        String requestJson = mapper.writeValueAsString(requestDto);

        when(userService.updateProfile(any(Authentication.class), eq(requestDto))).thenThrow(exception);

        mockMvc.perform(put("/api/v1/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().is(expectedStatus));
    }

    private static Stream<Arguments> updateProfileErrorCases() {
        return Stream.of(
                Arguments.of(new UserNotFoundException("No se encontró ningún usuario con ese email"), 404,
                        "usuario no encontrado"),
                Arguments.of(new EmailAlreadyExistsException("El email otro@pruebas.com ya está registrado."), 409,
                        "email ya registrado"));
    }

    @ParameterizedTest
    @MethodSource("invalidUpdateProfileRequests")
    @WithMockUser(username = "marta@pruebas.com")
    void testUpdateProfile_ShouldReturnBadRequest_WhenRequestIsInvalid(UpdateProfileRequestDTO invalidRequest)
            throws Exception {
        String requestJson = mapper.writeValueAsString(invalidRequest);

        mockMvc.perform(put("/api/v1/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    private static Stream<UpdateProfileRequestDTO> invalidUpdateProfileRequests() {
        return Stream.of(
                new UpdateProfileRequestDTO("", TEST_EMAIL, TEST_EMAIL, TEST_AVATAR_ID),
                new UpdateProfileRequestDTO(TEST_ALIAS, "not-an-email", "not-an-email", TEST_AVATAR_ID),
                new UpdateProfileRequestDTO(TEST_ALIAS, TEST_EMAIL, "", TEST_AVATAR_ID),
                new UpdateProfileRequestDTO(TEST_ALIAS, TEST_EMAIL, "otro@pruebas.com", TEST_AVATAR_ID),
                new UpdateProfileRequestDTO(TEST_ALIAS, TEST_EMAIL, TEST_EMAIL, null));
    }

}