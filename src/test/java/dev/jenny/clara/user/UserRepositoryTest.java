package dev.jenny.clara.user;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import dev.jenny.clara.role.RoleEntity;
import dev.jenny.clara.role.RoleRepository;

@DataJpaTest
@ActiveProfiles("h2")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private RoleEntity userRole;

    @BeforeEach
    void setUp() {
        userRole = roleRepository.findByName("USER").orElseThrow();
    }

    @Test
    void testFindByEmail() {
        UserEntity user = UserEntity.builder()
                .email("clara@pruebas.com")
                .passwordHash("hashed-password")
                .alias("clara")
                .role(userRole)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);

        Optional<UserEntity> found = userRepository.findByEmail("clara@pruebas.com");

        assertThat(found.isPresent(), is(true));
        assertThat(found.get().getEmail(), is(equalTo("clara@pruebas.com")));
    }
}
