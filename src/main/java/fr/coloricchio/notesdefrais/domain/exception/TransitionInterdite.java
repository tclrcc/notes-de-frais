package fr.coloricchio.notesdefrais.domain.exception;

import fr.coloricchio.notesdefrais.domain.model.StatutNote;

public final class TransitionInterdite extends RegleMetierViolee{
    public TransitionInterdite(String reference, StatutNote actuel, StatutNote cible) {
        super("Note %s : transition %s -> %s interdite".formatted(reference, actuel, cible));
    }
}
