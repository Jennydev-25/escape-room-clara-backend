package dev.jenny.clara.chat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    List<ChatMessageEntity> findByContactAndMinChapterLessThanEqualOrderByDisplayOrderAsc(String contact, int chapter);

    @Query("select distinct m.contact from ChatMessageEntity m where m.minChapter <= :chapter")
    List<String> findDistinctContactByMinChapterLessThanEqual(@Param("chapter") int chapter);

    boolean existsByContact(String contact);
}
