package fr.coloricchio.notesdefrais.infrastructure.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategorieDepenseJpaRepository extends JpaRepository<CategorieDepenseEntity, UUID> {
    Optional<CategorieDepenseEntity> findByCode(String code);
}
