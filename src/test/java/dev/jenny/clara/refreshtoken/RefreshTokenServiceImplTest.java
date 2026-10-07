package dev.jenny.clara.refreshtoken;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import dev.jenny.clara.refreshtoken.exceptions.InvalidRefreshTokenException;
import dev.jenny.clara.user.UserEntity;
import dev.jenny.clara.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {

    @Mock
    private RefreshTokenRepository repository;

    @Mock
    private UserRepository userRepository;

    private RefreshTokenServiceImpl service;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder().email("clara@example.com").build();
        service = new RefreshTokenServiceImpl(repository, userRepository, 365);
    }

    @Test
    void testCreateRefreshToken_ShouldSaveNewTokenForUser() {
        when(repository.save(any(RefreshTokenEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RefreshTokenEntity result = service.createRefreshToken(user);

        assertThat(result.getToken(), is(notNullValue()));
        assertThat(result.getUser(), is(equalTo(user)));
    }

    @Test
    void testCreateRefreshToken_ShouldResolveUserFromAuthentication() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("clara@example.com");
        when(userRepository.findByEmail("clara@example.com")).thenReturn(Optional.of(user));
        when(repository.save(any(RefreshTokenEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RefreshTokenEntity result = service.createRefreshToken(authentication);

        assertThat(result.getUser(), is(equalTo(user)));
    }

    @Test
    void testVerifyExpiration_ShouldThrowException_WhenTokenExpired() {
        RefreshTokenEntity expiredToken = RefreshTokenEntity.builder()
                .token("some-token")
                .user(user)
                .expiryDate(Instant.now().minus(1, ChronoUnit.DAYS))
                .build();

        assertThrows(InvalidRefreshTokenException.class, () -> service.verifyExpiration(expiredToken));

        verify(repository).delete(expiredToken);
    }

    @Test
    void testFindValidToken_ShouldReturnToken_WhenTokenExistsAndIsValid() {
        RefreshTokenEntity validToken = RefreshTokenEntity.builder()
                .token("valid-token")
                .user(user)
                .expiryDate(Instant.now().plus(1, ChronoUnit.DAYS))
                .build();

        when(repository.findByToken("valid-token")).thenReturn(Optional.of(validToken));

        RefreshTokenEntity result = service.findValidToken("valid-token");

        assertThat(result, is(equalTo(validToken)));
    }

    @Test
    void testFindValidToken_ShouldThrowException_WhenTokenDoesNotExist() {
        when(repository.findByToken("invalid-token")).thenReturn(Optional.empty());

        assertThrows(InvalidRefreshTokenException.class, () -> service.findValidToken("invalid-token"));
    }

    @Test
    void testRevokeRefreshToken_ShouldDeleteToken_WhenTokenBelongsToAuthenticatedUser() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("clara@example.com");

        RefreshTokenEntity token = RefreshTokenEntity.builder()
                .token("some-token")
                .user(user)
                .expiryDate(Instant.now().plus(1, ChronoUnit.DAYS))
                .build();

        when(repository.findByToken("some-token")).thenReturn(Optional.of(token));

        service.revokeRefreshToken("some-token", authentication);

        verify(repository).delete(token);
    }

    @Test
    void testRevokeRefreshToken_ShouldThrowException_WhenTokenDoesNotExist() {
        Authentication authentication = mock(Authentication.class);

        when(repository.findByToken("invalid-token")).thenReturn(Optional.empty());

        assertThrows(InvalidRefreshTokenException.class,
                () -> service.revokeRefreshToken("invalid-token", authentication));
    }
    
    @Test
    void testRevokeRefreshToken_ShouldThrowException_WhenTokenBelongsToAnotherUser() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("clara@example.com");

        UserEntity anotherUser = UserEntity.builder().email("intruder@example.com").build();
        RefreshTokenEntity token = RefreshTokenEntity.builder()
                .token("someone-elses-token")
                .user(anotherUser)
                .expiryDate(Instant.now().plus(1, ChronoUnit.DAYS))
                .build();

        when(repository.findByToken("someone-elses-token")).thenReturn(Optional.of(token));

        assertThrows(InvalidRefreshTokenException.class,
                () -> service.revokeRefreshToken("someone-elses-token", authentication));
    }
}
