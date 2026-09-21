package fr.coloricchio.notesdefrais.domain.port.in;

import java.util.UUID;

public interface CreerNote {
    UUID executer(CreerNoteCommande commande);
}
