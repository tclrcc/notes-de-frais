package fr.coloricchio.notesdefrais.domain.exception;

import fr.coloricchio.notesdefrais.domain.model.StatutNote;

public final class NoteNonModifiable extends RegleMetierViolee {
    public NoteNonModifiable(String reference, StatutNote statut) {
        super("Note %s : non modifiable au statut %s".formatted(reference, statut));
    }
}
