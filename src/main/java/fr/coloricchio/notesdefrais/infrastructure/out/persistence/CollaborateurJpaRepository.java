package fr.coloricchio.notesdefrais.infrastructure.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface CollaborateurJpaRepository extends JpaRepository<CollaborateurEntity, UUID> {
}
