package dev.jenny.clara.email;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailReadRepository extends JpaRepository<EmailReadEntity, Long> {

    Optional<EmailReadEntity> findByUserIdAndEmailId(Long userId, Long emailId);

    List<EmailReadEntity> findByUserId(Long userId);
}
