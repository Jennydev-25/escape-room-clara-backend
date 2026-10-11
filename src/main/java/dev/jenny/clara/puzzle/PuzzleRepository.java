package dev.jenny.clara.puzzle;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PuzzleRepository extends JpaRepository<PuzzleEntity, Long> {
}
