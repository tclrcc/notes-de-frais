package fr.coloricchio.notesdefrais.infrastructure.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EvenementStatutJpaRepository extends JpaRepository<EvenementStatutEntity, UUID> {}
