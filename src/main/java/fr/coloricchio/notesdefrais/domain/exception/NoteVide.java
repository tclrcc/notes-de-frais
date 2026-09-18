package fr.coloricchio.notesdefrais.domain.exception;

public final class NoteVide extends RegleMetierViolee {
    public NoteVide(String reference) {
        super("Note %s : impossible de soumettre une note sans aucune ligne de frais"
                .formatted(reference));
    }
}
