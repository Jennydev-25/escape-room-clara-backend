package dev.jenny.clara.progress;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<ProgressEntity, Long> {

    Optional<ProgressEntity> findByUserId(Long userId);

    List<ProgressEntity> findByInvestigationSubmittedTrueOrderByTimeSpentSecondsAsc();
}
