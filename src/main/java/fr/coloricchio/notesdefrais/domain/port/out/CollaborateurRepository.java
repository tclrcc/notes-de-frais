package fr.coloricchio.notesdefrais.domain.port.out;

import fr.coloricchio.notesdefrais.domain.model.Collaborateur;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CollaborateurRepository {
    Optional<Collaborateur> parId(UUID id);
    List<Collaborateur> tous();
}
