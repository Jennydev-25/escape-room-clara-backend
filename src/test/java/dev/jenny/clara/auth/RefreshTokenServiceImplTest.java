package dev.jenny.clara.auth;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.jenny.clara.user.User;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {

    @Mock
    private RefreshTokenRepository repository;

    private RefreshTokenServiceImpl service;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().email("clara@example.com").build();
        service = new RefreshTokenServiceImpl(repository, 365);
    }

    @Test
    void testCreateRefreshToken_ShouldSaveNewTokenForUser() {
        when(repository.save(any(RefreshTokenEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RefreshTokenEntity result = service.createRefreshToken(user);

        verify(repository).deleteByUser(user);
        assertThat(result.getToken(), is(notNullValue()));
        assertThat(result.getUser(), is(equalTo(user)));
    }

    @Test
    void testVerifyExpiration_ShouldThrowException_WhenTokenExpired() {
        RefreshTokenEntity expiredToken = new RefreshTokenEntity(
                "some-token",
                user,
                Instant.now().minus(1, ChronoUnit.DAYS));

        assertThrows(InvalidRefreshTokenException.class, () -> service.verifyExpiration(expiredToken));

        verify(repository).delete(expiredToken);
    }
}