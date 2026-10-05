package dev.jenny.clara.contact.mappers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

import dev.jenny.clara.contact.ContactMessageEntity;
import dev.jenny.clara.contact.ContactType;
import dev.jenny.clara.contact.dtos.ContactRequestDTO;
import dev.jenny.clara.user.UserEntity;

class ContactMessageMapperTest {

    private static final String TEST_NAME = "Marta";
    private static final String TEST_EMAIL = "marta@example.com";
    private static final String TEST_MESSAGE = "Tengo un problema con el capítulo 2.";

    @Test
    void testConstructor_ShouldThrowException_WhenInstantiated() throws Exception {
        Constructor<ContactMessageMapper> constructor = ContactMessageMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class,
                constructor::newInstance);

        assertThat(exception.getCause(), instanceOf(UnsupportedOperationException.class));
    }

    @Test
    void testToEntity_ShouldMapRequestFieldsToContactMessageEntity() {
        ContactRequestDTO request = new ContactRequestDTO(TEST_NAME, TEST_EMAIL, ContactType.QUESTION, TEST_MESSAGE,
                "recaptcha-token");
        UserEntity user = UserEntity.builder().email(TEST_EMAIL).build();

        ContactMessageEntity entity = ContactMessageMapper.toEntity(request, user);

        assertThat(entity, instanceOf(ContactMessageEntity.class));
        assertThat(entity.getName(), is(equalTo(request.name())));
        assertThat(entity.getEmail(), is(equalTo(request.email())));
        assertThat(entity.getType(), is(equalTo(request.type())));
        assertThat(entity.getMessage(), is(equalTo(request.message())));
        assertThat(entity.getUser(), is(equalTo(user)));
        assertThat(entity.getCreatedAt(), is(notNullValue()));
    }
}
