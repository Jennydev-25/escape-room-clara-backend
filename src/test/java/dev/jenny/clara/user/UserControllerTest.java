package dev.jenny.clara.user;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.jenny.clara.config.SecurityConfig;
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
}