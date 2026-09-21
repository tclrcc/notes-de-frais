package fr.coloricchio.notesdefrais.domain.port.in;

import java.util.UUID;

public record DeciderSurNoteCommande(UUID noteId, UUID decideurId, String motif) {}
