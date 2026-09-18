package fr.coloricchio.notesdefrais.domain.model;

import java.time.Instant;
import java.util.UUID;

/** Evènement : changement de statut d'une note de frais */
public record EvenementNote(
        UUID id,
        UUID noteId,
        String reference,
        StatutNote statutPrecedent,
        StatutNote statutNouveau,
        UUID auteurId,
        Instant survenuLe,
        String motif
) {
    public String cleDePartitionnement() {
        return noteId.toString();
    }
}
