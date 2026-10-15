package dev.jenny.clara.seen;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SeenItemRepository extends JpaRepository<SeenItemEntity, Long> {

    List<SeenItemEntity> findByUserId(Long userId);
}
