package dev.jenny.clara.register.mappers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

import dev.jenny.clara.register.dtos.RegisterRequestDTO;
import dev.jenny.clara.user.Role;
import dev.jenny.clara.user.UserEntity;

class RegisterMapperTest {

    private static final String TEST_EMAIL = "marta@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_HASHED_PASSWORD = "$2a$10$hashedExamplePassword";
    private static final String TEST_ALIAS = "marta";

    @Test
    void testConstructor_ShouldThrowException_WhenInstantiated() throws Exception {
        Constructor<RegisterMapper> constructor = RegisterMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class,
                constructor::newInstance);

        assertThat(exception.getCause(), instanceOf(UnsupportedOperationException.class));
    }

    @Test
    void testToEntity_ShouldMapRequestFieldsToUserEntity() {
        RegisterRequestDTO request = new RegisterRequestDTO(TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD,
                "recaptcha-token");

        UserEntity entity = RegisterMapper.toEntity(request, TEST_HASHED_PASSWORD, TEST_ALIAS);

        assertThat(entity, instanceOf(UserEntity.class));
        assertThat(entity.getEmail(), is(equalTo(request.email())));
        assertThat(entity.getPasswordHash(), is(equalTo(TEST_HASHED_PASSWORD)));
        assertThat(entity.getAlias(), is(equalTo(TEST_ALIAS)));
        assertThat(entity.getRole(), is(equalTo(Role.USER)));
        assertThat(entity.getCreatedAt(), is(notNullValue()));
    }
}