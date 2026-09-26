package fr.coloricchio.notesdefrais.infrastructure.out.persistence;

import fr.coloricchio.notesdefrais.domain.model.Collaborateur;
import fr.coloricchio.notesdefrais.domain.port.out.CollaborateurRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CollaborateurRepositoryAdapter implements CollaborateurRepository {

    private static final Sort PAR_NOM = Sort.by("nom", "prenom");

    private final CollaborateurJpaRepository jpa;

    public CollaborateurRepositoryAdapter(CollaborateurJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Collaborateur> parId(UUID id) {
        return jpa.findById(id).map(PersistenceMapper::versDomaine);
    }

    @Override
    public List<Collaborateur> tous() {
        return jpa.findAll(PAR_NOM).stream()
                .map(PersistenceMapper::versDomaine)
                .toList();
    }
}
