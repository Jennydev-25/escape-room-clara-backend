package dev.jenny.clara.puzzle;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PuzzleRepository extends JpaRepository<PuzzleEntity, Long> {

    List<PuzzleEntity> findByFolderIdAndMinChapterLessThanEqualOrderByDisplayOrderAsc(Long folderId, int minChapter);
}
