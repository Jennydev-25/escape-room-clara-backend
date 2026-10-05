package dev.jenny.clara.contact.mappers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

class ContactMessageMapperTest {

    @Test
    void testConstructor_ShouldThrowException_WhenInstantiated() throws Exception {
        Constructor<ContactMessageMapper> constructor = ContactMessageMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class,
                constructor::newInstance);

        assertThat(exception.getCause(), instanceOf(UnsupportedOperationException.class));
    }
}
