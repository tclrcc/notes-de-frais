package fr.coloricchio.notesdefrais.domain.exception;

import java.util.UUID;

public final class NoteIntrouvable extends RegleMetierViolee {
    public NoteIntrouvable(UUID id) {
        super("Aucune note de frais avec l'identifiant %s".formatted(id));
    }
}
