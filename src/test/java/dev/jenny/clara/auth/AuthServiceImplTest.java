package dev.jenny.clara.auth;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import dev.jenny.clara.auth.dtos.LoginResponseDTO;
import dev.jenny.clara.user.User;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private InterfaceRefreshTokenService refreshTokenService;

    private AuthServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AuthServiceImpl(jwtService, refreshTokenService);
    }

    @Test
    void testLogin_ShouldReturnAccessAndRefreshTokens() {
        Authentication authentication = mock(Authentication.class);
        String token = "fake.jwt.token";
        String refreshToken = "fake-refresh-token";
        RefreshTokenEntity refreshTokenEntity = mock(RefreshTokenEntity.class);

        when(jwtService.generateToken(authentication)).thenReturn(token);
        when(refreshTokenService.createRefreshToken(authentication)).thenReturn(refreshTokenEntity);
        when(refreshTokenEntity.getToken()).thenReturn(refreshToken);

        LoginResponseDTO result = service.login(authentication);

        assertThat(result, is(equalTo(new LoginResponseDTO(token, refreshToken))));
    }

    @Test
    void testRefresh_ShouldReturnNewAccessAndRefreshTokens() {
        String oldRefreshToken = "old-refresh-token";
        String newToken = "new.jwt.token";
        String newRefreshToken = "new-refresh-token";

        User user = mock(User.class);
        RefreshTokenEntity validToken = mock(RefreshTokenEntity.class);
        RefreshTokenEntity newRefreshTokenEntity = mock(RefreshTokenEntity.class);

        when(refreshTokenService.findValidToken(oldRefreshToken)).thenReturn(validToken);
        when(validToken.getUser()).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(newToken);
        when(refreshTokenService.createRefreshToken(user)).thenReturn(newRefreshTokenEntity);
        when(newRefreshTokenEntity.getToken()).thenReturn(newRefreshToken);

        LoginResponseDTO result = service.refresh(oldRefreshToken);

        assertThat(result, is(equalTo(new LoginResponseDTO(newToken, newRefreshToken))));
    }
}