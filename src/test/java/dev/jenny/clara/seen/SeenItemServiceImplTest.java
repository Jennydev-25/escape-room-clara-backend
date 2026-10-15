package dev.jenny.clara.seen;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import dev.jenny.clara.user.UserEntity;
import dev.jenny.clara.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class SeenItemServiceImplTest {

    private static final String USER_EMAIL = "clara@pruebas.com";
    private static final Long USER_ID = 10L;
    private static final String FOLDER_KEY = "folder:leeme";
    private static final String FILE_KEY = "file:/tomas/tomas_elorza.png";

    @Mock
    private SeenItemRepository seenItemRepository;

    @Mock
    private UserRepository userRepository;

    private SeenItemServiceImpl service;

    private Authentication authentication;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        service = new SeenItemServiceImpl(seenItemRepository, userRepository);
        authentication = org.mockito.Mockito.mock(Authentication.class);
        user = UserEntity.builder().id(USER_ID).email(USER_EMAIL).build();
    }

    private void stubAuthenticatedUser() {
        when(authentication.getName()).thenReturn(USER_EMAIL);
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
    }

    @Test
    void testGetSeenKeys_ShouldReturnKeysOfTheAuthenticatedUser() {
        stubAuthenticatedUser();
        when(seenItemRepository.findByUserId(USER_ID)).thenReturn(List.of(
                SeenItemEntity.builder().user(user).itemKey(FOLDER_KEY).build(),
                SeenItemEntity.builder().user(user).itemKey(FILE_KEY).build()));

        List<String> result = service.getSeenKeys(authentication);

        assertThat(result, contains(FOLDER_KEY, FILE_KEY));
    }

    @Test
    void testMarkSeen_ShouldSaveTheKey_WhenUserHasNotSeenItYet() {
        stubAuthenticatedUser();
        when(seenItemRepository.existsByUserIdAndItemKey(USER_ID, FOLDER_KEY)).thenReturn(false);

        service.markSeen(authentication, FOLDER_KEY);

        verify(seenItemRepository).save(any(SeenItemEntity.class));
    }
}
