package fr.coloricchio.notesdefrais.domain.port.in;

import fr.coloricchio.notesdefrais.domain.model.Periode;

import java.util.UUID;

public record CreerNoteCommande(UUID collaborateurId, Periode periode) {}
