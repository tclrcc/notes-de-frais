package fr.coloricchio.notesdefrais.infrastructure.out.messaging;

import fr.coloricchio.notesdefrais.domain.model.EvenementNote;

import java.time.Instant;
import java.util.UUID;

public record EvenementNoteMessage(
        UUID evenementId,
        UUID noteId,
        String reference,
        String statutPrecedent,
        String statutNouveau,
        UUID auteurId,
        Instant survenuLe,
        String motif
) {
    public static EvenementNoteMessage depuis(EvenementNote e) {
        return new EvenementNoteMessage(
                e.id(), e.noteId(), e.reference(),
                e.statutPrecedent() == null ? null : e.statutPrecedent().name(),
                e.statutNouveau().name(),
                e.auteurId(), e.survenuLe(), e.motif());
    }
}
