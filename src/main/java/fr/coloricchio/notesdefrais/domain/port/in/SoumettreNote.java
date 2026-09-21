package fr.coloricchio.notesdefrais.domain.port.in;

public interface SoumettreNote {
    void executer(SoumettreNoteCommande commande);
}
