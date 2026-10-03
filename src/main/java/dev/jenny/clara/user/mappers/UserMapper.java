package dev.jenny.clara.user.mappers;

import dev.jenny.clara.user.UserEntity;
import dev.jenny.clara.user.dtos.UserProfileResponseDTO;

public class UserMapper {

    private UserMapper() {
        throw new UnsupportedOperationException("No se puede instanciar esta clase de utilidad");
    }

    public static UserProfileResponseDTO toDTO(UserEntity entity) {
        return new UserProfileResponseDTO(
                entity.getAlias(),
                entity.getEmail(),
                entity.getAvatarId());
    }
}