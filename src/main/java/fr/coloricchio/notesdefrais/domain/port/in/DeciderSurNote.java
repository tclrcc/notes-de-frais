package fr.coloricchio.notesdefrais.domain.port.in;

public interface DeciderSurNote {
    void valider(DeciderSurNoteCommande commande);
    void rejeter(DeciderSurNoteCommande commande);
    void rembourser(DeciderSurNoteCommande commande);
}
