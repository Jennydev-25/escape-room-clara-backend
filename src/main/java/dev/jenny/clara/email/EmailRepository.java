package dev.jenny.clara.email;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<EmailEntity, Long> {

    List<EmailEntity> findByMinChapterLessThanEqualOrderByMinChapterAsc(int chapter);
}
