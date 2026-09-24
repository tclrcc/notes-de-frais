package fr.coloricchio.notesdefrais.domain.exception;

import java.util.UUID;

public final class CollaborateurIntrouvable extends RegleMetierViolee {

    public CollaborateurIntrouvable(UUID id) {
        super("Aucun collaborateur ne correspond à l'identifiant %s".formatted(id));
    }
}
