package dev.jenny.clara.role;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository repository;

    private RoleServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new RoleServiceImpl(repository);
    }

    @Test
    void testAssignDefaultRole_ShouldReturnDefaultRole_WhenItExists() {
        RoleEntity defaultRole = RoleEntity.builder().id(1L).name("USER").build();
        when(repository.findByName("USER")).thenReturn(Optional.of(defaultRole));

        RoleEntity result = service.assignDefaultRole();

        assertThat(result, is(equalTo(defaultRole)));
    }

    @Test
    void testAssignDefaultRole_ShouldThrowException_WhenDefaultRoleDoesNotExist() {
        when(repository.findByName("USER")).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> service.assignDefaultRole());
    }
}
