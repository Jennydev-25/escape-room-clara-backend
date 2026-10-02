package dev.jenny.clara.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import dev.jenny.clara.user.User;
import dev.jenny.clara.user.UserRepository;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final long accessTokenExpirationHours;

    public JwtService(JwtEncoder jwtEncoder, UserRepository userRepository,
            @Value("${jwt.access-token.expiration-hours}") long accessTokenExpirationHours) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.accessTokenExpirationHours = accessTokenExpirationHours;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        User user = userRepository.findByEmail(authentication.getName()).get();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(authentication.getName())
                .expiresAt(now.plus(accessTokenExpirationHours, ChronoUnit.HOURS))
                .claim("role", user.getRole().name())
                .build();

        var encoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS512).build(), claims);
        return jwtEncoder.encode(encoderParameters).getTokenValue();
    }
}