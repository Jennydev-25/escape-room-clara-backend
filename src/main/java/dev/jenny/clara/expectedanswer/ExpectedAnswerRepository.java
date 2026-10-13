package dev.jenny.clara.expectedanswer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpectedAnswerRepository extends JpaRepository<ExpectedAnswerEntity, Long> {

    List<ExpectedAnswerEntity> findByPuzzleId(Long puzzleId);
}
