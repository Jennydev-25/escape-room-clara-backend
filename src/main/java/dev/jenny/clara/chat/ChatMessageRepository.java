package dev.jenny.clara.chat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    List<ChatMessageEntity> findByContactAndMinChapterLessThanEqualOrderByDisplayOrderAsc(String contact, int chapter);
}
