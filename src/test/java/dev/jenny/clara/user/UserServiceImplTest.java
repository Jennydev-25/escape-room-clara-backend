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
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.jenny.clara.register.exceptions.EmailAlreadyExistsException;
import dev.jenny.clara.user.dtos.ChangePasswordRequestDTO;
import dev.jenny.clara.user.dtos.UpdateProfileRequestDTO;
import dev.jenny.clara.user.dtos.UserProfileResponseDTO;
import dev.jenny.clara.user.exceptions.InvalidCurrentPasswordException;
import dev.jenny.clara.user.exceptions.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private static final String TEST_EMAIL = "marta@example.com";
    private static final String TEST_ALIAS = "marta_v";
    private static final Integer TEST_AVATAR_ID = 3;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, passwordEncoder);
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

    @Test
    void testUpdateProfile_ShouldReturnUpdatedProfile_WhenUserExists() {
        UserEntity user = UserEntity.builder()
                .email(TEST_EMAIL)
                .alias(TEST_ALIAS)
                .avatarId(TEST_AVATAR_ID)
                .build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(TEST_EMAIL);
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));

        UpdateProfileRequestDTO request = new UpdateProfileRequestDTO("nuevo_alias", TEST_EMAIL, TEST_EMAIL, 5);

        UserProfileResponseDTO result = userService.updateProfile(authentication, request);

        assertThat(result.alias(), is(equalTo("nuevo_alias")));
        assertThat(result.email(), is(equalTo(TEST_EMAIL)));
        assertThat(result.avatarId(), is(equalTo(5)));
    }

    @Test
    void testUpdateProfile_ShouldThrowEmailAlreadyExistsException_WhenNewEmailBelongsToAnotherUser() {
        UserEntity user = UserEntity.builder()
                .email(TEST_EMAIL)
                .alias(TEST_ALIAS)
                .avatarId(TEST_AVATAR_ID)
                .build();

        String otherEmail = "otro@example.com";

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(TEST_EMAIL);
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(otherEmail)).thenReturn(Optional.of(mock(UserEntity.class)));

        UpdateProfileRequestDTO request = new UpdateProfileRequestDTO(TEST_ALIAS, otherEmail, otherEmail,
                TEST_AVATAR_ID);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.updateProfile(authentication, request));
    }

    @Test
    void testUpdateProfile_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(TEST_EMAIL);
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        UpdateProfileRequestDTO request = new UpdateProfileRequestDTO(TEST_ALIAS, TEST_EMAIL, TEST_EMAIL,
                TEST_AVATAR_ID);

        assertThrows(UserNotFoundException.class, () -> userService.updateProfile(authentication, request));
    }

    @Test
    void testUpdateProfile_ShouldReturnUpdatedProfile_WhenEmailChangesToAvailableEmail() {
        UserEntity user = UserEntity.builder()
                .email(TEST_EMAIL)
                .alias(TEST_ALIAS)
                .avatarId(TEST_AVATAR_ID)
                .build();

        String newEmail = "nuevo@example.com";

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(TEST_EMAIL);
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(newEmail)).thenReturn(Optional.empty());

        UpdateProfileRequestDTO request = new UpdateProfileRequestDTO(TEST_ALIAS, newEmail, newEmail, TEST_AVATAR_ID);

        UserProfileResponseDTO result = userService.updateProfile(authentication, request);

        assertThat(result.email(), is(equalTo(newEmail)));
    }

    @Test
    void testChangePassword_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(TEST_EMAIL);
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("oldPass123", "newPass123", "newPass123");

        assertThrows(UserNotFoundException.class, () -> userService.changePassword(authentication, request));
    }

    @Test
    void testChangePassword_ShouldThrowInvalidCurrentPasswordException_WhenCurrentPasswordIsIncorrect() {
        UserEntity user = UserEntity.builder()
                .email(TEST_EMAIL)
                .passwordHash("hashedPassword")
                .build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(TEST_EMAIL);
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPass123", "hashedPassword")).thenReturn(false);

        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("wrongPass123", "newPass123", "newPass123");

        assertThrows(InvalidCurrentPasswordException.class, () -> userService.changePassword(authentication, request));
    }
}