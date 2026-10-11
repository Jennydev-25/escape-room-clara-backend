package dev.jenny.clara.chat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageReadRepository extends JpaRepository<ChatMessageReadEntity, Long> {

    List<ChatMessageReadEntity> findByUserId(Long userId);
}
