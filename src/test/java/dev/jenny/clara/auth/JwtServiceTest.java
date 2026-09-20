package dev.jenny.clara.auth;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import dev.jenny.clara.user.Role;
import dev.jenny.clara.user.User;
import dev.jenny.clara.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService service;

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private UserRepository userRepository;

    @ParameterizedTest
    @MethodSource("rolesAndExpectedClaim")
    void testGenerateToken_ShouldIncludeUserRoleAsClaim(Role role, String expectedClaim) {
        User user = User.builder()
                .email("clara@pruebas.com")
                .passwordHash("hashedPassword")
                .alias("clara")
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("clara@pruebas.com");
        when(userRepository.findByEmail("clara@pruebas.com")).thenReturn(Optional.of(user));

        Jwt fakeJwt = mock(Jwt.class);
        when(fakeJwt.getTokenValue()).thenReturn("fake-token");
        when(jwtEncoder.encode(any())).thenReturn(fakeJwt);

        String result = service.generateToken(authentication);

        ArgumentCaptor<JwtEncoderParameters> paramsCaptor = ArgumentCaptor.forClass(JwtEncoderParameters.class);
        verify(jwtEncoder).encode(paramsCaptor.capture());
        String role_ = paramsCaptor.getValue().getClaims().getClaim("role");

        assertThat(role_, is(equalTo(expectedClaim)));
        assertThat(result, is(equalTo("fake-token")));
    }

    private static Stream<Arguments> rolesAndExpectedClaim() {
        return Stream.of(
                Arguments.of(Role.USER, "USER"),
                Arguments.of(Role.ADMIN, "ADMIN"));
    }
}