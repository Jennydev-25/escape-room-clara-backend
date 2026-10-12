package dev.jenny.clara.folder;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<FolderEntity, Long> {

    Optional<FolderEntity> findByCode(String code);

    List<FolderEntity> findByMinChapterToOpen(int minChapterToOpen);

    List<FolderEntity> findByMinChapterToOpenLessThanEqualOrderByMinChapterToOpenAsc(int chapter);
}
