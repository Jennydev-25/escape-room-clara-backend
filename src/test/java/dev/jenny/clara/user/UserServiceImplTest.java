package dev.jenny.clara.user;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import dev.jenny.clara.user.dtos.UserProfileResponseDTO;
import dev.jenny.clara.user.exceptions.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private static final String TEST_EMAIL = "marta@example.com";
    private static final String TEST_ALIAS = "marta_v";
    private static final Integer TEST_AVATAR_ID = 3;

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void testGetProfile_ShouldReturnUserProfile_WhenUserExists() {
        UserEntity user = UserEntity.builder()
                .email(TEST_EMAIL)
                .alias(TEST_ALIAS)
                .avatarId(TEST_AVATAR_ID)
                .build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(TEST_EMAIL);
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));

        UserProfileResponseDTO result = userService.getProfile(authentication);

        assertThat(result.alias(), is(equalTo(TEST_ALIAS)));
        assertThat(result.email(), is(equalTo(TEST_EMAIL)));
        assertThat(result.avatarId(), is(equalTo(TEST_AVATAR_ID)));
    }

    @Test
    void testGetProfile_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(TEST_EMAIL);
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getProfile(authentication));
    }
}