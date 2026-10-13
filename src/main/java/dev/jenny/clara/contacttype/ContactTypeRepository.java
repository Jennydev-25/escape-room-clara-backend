package dev.jenny.clara.contacttype;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactTypeRepository extends JpaRepository<ContactTypeEntity, Long> {

    Optional<ContactTypeEntity> findByName(String name);

}
