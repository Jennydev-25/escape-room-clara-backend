package dev.jenny.clara.trash;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscardedFileRepository extends JpaRepository<DiscardedFileEntity, Long> {

    List<DiscardedFileEntity> findByMinChapterLessThanEqualOrderByMinChapterAsc(int chapter);
}
