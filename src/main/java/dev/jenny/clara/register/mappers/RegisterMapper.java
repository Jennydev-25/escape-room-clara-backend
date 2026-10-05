package dev.jenny.clara.register.mappers;

import java.time.LocalDateTime;

import dev.jenny.clara.register.dtos.RegisterRequestDTO;
import dev.jenny.clara.user.Role;
import dev.jenny.clara.user.UserEntity;

public class RegisterMapper {

    private RegisterMapper() {
        throw new UnsupportedOperationException("No se puede instanciar esta clase de utilidad");
    }

    public static UserEntity toEntity(RegisterRequestDTO request, String hashedPassword, String alias) {
        return UserEntity.builder()
                .email(request.email())
                .passwordHash(hashedPassword)
                .alias(alias)
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();
    }
}