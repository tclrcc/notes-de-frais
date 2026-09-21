package fr.coloricchio.notesdefrais.infrastructure.out.persistence;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteDeFraisJpaRepository extends JpaRepository<NoteDeFraisEntity, UUID> {

    @EntityGraph(attributePaths = {"lignes", "lignes.categorie"})
    Optional<NoteDeFraisEntity> findWithLignesById(UUID id);

    @EntityGraph(attributePaths = {"lignes", "lignes.categorie"})
    List<NoteDeFraisEntity> findByCollaborateurId(UUID collaborateurId);

    @Query("select count(n) from NoteDeFraisEntity n where n.periode = :periode")
    long compterSurPeriode(@Param("periode")LocalDate periode);
}
