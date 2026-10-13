package dev.jenny.clara.user.mappers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

import dev.jenny.clara.user.UserEntity;
import dev.jenny.clara.user.dtos.UserProfileResponseDTO;

class UserMapperTest {

    private static final String TEST_ALIAS = "marta_v";
    private static final String TEST_EMAIL = "marta@example.com";
    private static final Integer TEST_AVATAR_ID = 3;
    private static final String TEST_FULL_NAME = "Marta Valle";

    @Test
    void testToDTO_ShouldMapEntityFieldsToResponseDTO() {
        UserEntity entity = UserEntity.builder()
                .alias(TEST_ALIAS)
                .email(TEST_EMAIL)
                .avatarId(TEST_AVATAR_ID)
                .fullName(TEST_FULL_NAME)
                .build();

        UserProfileResponseDTO dto = UserMapper.toDTO(entity);

        assertThat(dto, isA(UserProfileResponseDTO.class));
        assertThat(dto.alias(), is(equalTo(entity.getAlias())));
        assertThat(dto.email(), is(equalTo(entity.getEmail())));
        assertThat(dto.avatarId(), is(equalTo(entity.getAvatarId())));
        assertThat(dto.fullName(), is(equalTo(entity.getFullName())));
    }

    @Test
    void testConstructor_ShouldThrowException_WhenInstantiatedViaReflection() throws Exception {
        Constructor<UserMapper> constructor = UserMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        assertThat(exception.getCause(), isA(UnsupportedOperationException.class));
    }
}