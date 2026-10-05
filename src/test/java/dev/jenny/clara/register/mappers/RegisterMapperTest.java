package dev.jenny.clara.register.mappers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

class RegisterMapperTest {

    @Test
    void testConstructor_ShouldThrowException_WhenInstantiated() throws Exception {
        Constructor<RegisterMapper> constructor = RegisterMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class,
                constructor::newInstance);

        assertThat(exception.getCause(), instanceOf(UnsupportedOperationException.class));
    }
}